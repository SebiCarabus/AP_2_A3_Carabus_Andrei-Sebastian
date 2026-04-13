package advanced;

import advanced.importer.CreditsImporter;
import advanced.importer.MovieImporter;
import advanced.objects.Movie;
import advanced.dao.ActorDAO;
import advanced.dao.GenreDAO;
import advanced.dao.MovieDAO;
import advanced.database.Database;
import advanced.partition.MoviePartitionService;
import advanced.report.ReportService;
import org.flywaydb.core.Flyway;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            Flyway flyway = Flyway.configure()
                    .dataSource("jdbc:postgresql://localhost:5432/javaLab6", "postgres", "password")
                    .locations("classpath:db/migration")
                    .load();
            flyway.repair();
            flyway.migrate();

            MovieImporter metadataImporter = new MovieImporter();
            System.out.println("Importing movie metadata...");
            metadataImporter.importMovies("src/main/resources/data/movies_metadata.csv");


            CreditsImporter creditsImporter = new CreditsImporter();
            System.out.println("Importing actors and credits (this might take a while)...");
            creditsImporter.importCredits("src/main/resources/data/credits.csv");

            MovieDAO movieDAO = new MovieDAO();
            List<Movie> movies = movieDAO.findAll();
            Map<Integer, Set<Integer>> adjacency = movieDAO.getMovieAdjacencyList();

            MoviePartitionService service = new MoviePartitionService();
            List<List<Movie>> movieLists = service.partition(movies, adjacency);

            System.out.println("\nFinished Partition (there are "+movieLists.size()+" partitions):\n");
            for (int i = 0; i < movieLists.size(); i++) {
                System.out.println("List " + (i + 1) + " has " + movieLists.get(i).size() + " movies:");
                movieLists.get(i).forEach(movie -> {
                    System.out.println(movie.getTitle());
                });
                System.out.println();
            }
        } catch (Exception exception){
            System.err.println("Error: "+exception);
        }
    }

}