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