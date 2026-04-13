package advanced.importer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import advanced.dao.ActorDAO;
import advanced.dao.MovieDAO;
import advanced.objects.Actor;
import advanced.objects.Movie;

import java.io.FileReader;

public class CreditsImporter {
    private final ObjectMapper mapper = new ObjectMapper();

    public void importCredits(String path) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] line;
            reader.readNext();

            MovieDAO movieDAO = new MovieDAO();
            ActorDAO actorDAO = new ActorDAO();
            int count = 0;

            while ((line = reader.readNext()) != null && count < 1000) {
                try {
                    String castJsonRaw = line[0];

                    int movieId = Integer.parseInt(line[2]);

                    Movie movie = movieDAO.findById(movieId);
                    if (movie == null) {
                        continue;
                    }

                    String cleanJson = castJsonRaw
                            .replace("'", "\"")
                            .replace(": None", ": null");

                    JsonNode rootNode = mapper.readTree(cleanJson);
                    if (rootNode.isArray()) {
                        for (JsonNode actorNode : rootNode) {
                            String actorName = actorNode.path("name").asText();
                            if (actorName == null || actorName.isEmpty()) continue;

                            Actor actor = actorDAO.findByName(actorName);
                            if (actor == null) {
                                actor = actorDAO.create(actorName);
                            }
                            movie.addActor(actor);
                        }
                    }
                    count++;
                } catch (Exception e) {
                    continue;
                }
            }
        }
    }
}