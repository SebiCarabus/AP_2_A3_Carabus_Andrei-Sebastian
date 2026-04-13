package advanced.importer;

import com.opencsv.CSVReader;
import advanced.dao.MovieDAO;
import advanced.dao.GenreDAO;
import advanced.objects.Movie;
import advanced.objects.Genre;
import java.io.FileReader;
import java.sql.Date;

public class MovieImporter {
    public void importMovies(String path) throws Exception {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] line;
            reader.readNext();

            MovieDAO movieDAO = new MovieDAO();
            GenreDAO genreDAO = new GenreDAO();
            int count = 0;

            while ((line = reader.readNext()) != null && count < 2000) {
                try {

                    int id = Integer.parseInt(line[5]);
                    String title = line[20];
                    String releaseDateStr = line[14];
                    String durationStr = line[16];
                    String scoreStr = line[22];
                    String genresRaw = line[3];

                    Movie movie = movieDAO.findByTitle(title);

                    if (movie == null) {

                        movie = movieDAO.create(id,title);

                        if (!releaseDateStr.isEmpty()) {
                            movie.setReleaseDate(Date.valueOf(releaseDateStr));
                        }
                        if (!durationStr.isEmpty()) {
                            movie.setDuration((int) Double.parseDouble(durationStr));
                        }
                        if (!scoreStr.isEmpty()) {
                            movie.setScore(Float.parseFloat(scoreStr));
                        }

                        if (genresRaw.contains("'name': '")) {
                            String[] parts = genresRaw.split("'name': '");
                            for (int i = 1; i < parts.length; i++) {
                                String genreName = parts[i].split("'")[0];

                                Genre genre = genreDAO.findByName(genreName);
                                if (genre == null) {
                                    genre = genreDAO.create(genreName);
                                }
                                movie.addGenre(genre);
                            }
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