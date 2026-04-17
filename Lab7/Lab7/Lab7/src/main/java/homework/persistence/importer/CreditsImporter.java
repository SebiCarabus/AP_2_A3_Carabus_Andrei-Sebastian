package homework.persistence.importer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import homework.domain.Actor;
import homework.domain.Movie;
import homework.persistence.dao.ActorDAO;
import homework.persistence.dao.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditsImporter {
    private final ObjectMapper mapper = new ObjectMapper()
            .configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private ActorDAO actorDAO;

    public void importCredits(String path) throws Exception {
        org.springframework.core.io.Resource resource = new org.springframework.core.io.ClassPathResource(path);

        try (CSVReader reader = new CSVReader(new java.io.InputStreamReader(resource.getInputStream(), java.nio.charset.StandardCharsets.UTF_8))) {
            String[] line;
            reader.readNext();
            int count = 0;

            while ((line = reader.readNext()) != null && count < 1000) {
                try {
                    String castJsonRaw = line[0];
                    String cleanJson = castJsonRaw.replace("None", "null");

                    int movieId;
                    try {
                        movieId = Integer.parseInt(line[2]);
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    Movie movie = movieDAO.findById(movieId);
                    if (movie == null)
                        continue;

                    JsonNode rootNode = mapper.readTree(cleanJson);
                    if (rootNode.isArray()) {
                        for (JsonNode actorNode : rootNode) {
                            String actorName = actorNode.path("name").asText();
                            if (actorName == null || actorName.isEmpty())
                                continue;

                            Actor actor = actorDAO.findByName(actorName);
                            if (actor == null) {
                                actor = actorDAO.create(actorName);
                            }
                            movieDAO.addActor(movie, actor);
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