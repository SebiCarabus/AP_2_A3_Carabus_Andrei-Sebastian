package advanced;

import advanced.persistence.importer.CreditsImporter;
import advanced.persistence.importer.MoviesImporter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public CommandLineRunner run(MoviesImporter movieImporter, CreditsImporter creditsImporter) {
		return args -> {
			System.out.println("Starting import...");
			movieImporter.importMovies("data/movies.csv");
			creditsImporter.importCredits("data/credits.csv");
			System.out.println("Import finished.");
		};
	}

}
