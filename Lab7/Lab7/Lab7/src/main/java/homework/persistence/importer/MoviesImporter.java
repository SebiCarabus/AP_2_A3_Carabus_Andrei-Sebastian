package homework.persistence.importer;

import com.opencsv.CSVReader;
import homework.domain.Genre;
import homework.domain.Movie;
import homework.persistence.dao.GenreDAO;
import homework.persistence.dao.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class MoviesImporter {
    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private GenreDAO genreDAO;

    public void importMovies(String path) throws Exception {
        org.springframework.core.io.Resource resource = new org.springframework.core.io.ClassPathResource(path.replace("classpath:", ""));
        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String[] line;
            reader.readNext();

            int count = 0;

            while ((line = reader.readNext()) != null && count < 2000) {
                try {
                    int id = Integer.parseInt(line[0]);
                    String title = line[1];
                    String genresRaw = line[2];

                    Movie movie = movieDAO.findByTitle(title);
                    if (movie == null) {
                        movie = movieDAO.create(id, title);
                        if (genresRaw != null && !genresRaw.isEmpty() && !genresRaw.equals("(no genres listed)")) {
                            String[] genres = genresRaw.split("\\|");
                            for (String genreName : genres) {
                                Genre genre = genreDAO.findByName(genreName);
                                if (genre == null) {
                                    genre = genreDAO.create(genreName);
                                }
                                movieDAO.addGenre(movie,genre);
                                break;
                            }
                        }
                    }
                    count++;
                } catch (Exception exception) {
                    continue;
                }
            }
        }
    }
}