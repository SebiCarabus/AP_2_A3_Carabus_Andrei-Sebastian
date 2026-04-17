package Lab7_client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import java.util.List;


@Component
public class MovieClientApp implements CommandLineRunner {

    private final RestClient restClient;

    public MovieClientApp() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081/api/v1")
                .build();
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Pornire Client HTTP ---");

        List<MovieDTO> movies = restClient.get()
                .uri("/movies")
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<MovieDTO>>() {});

        System.out.println("Filme primite de la server: " + movies.size());

        MovieDTO newMovie = new MovieDTO("Interstellar", "2014-11-07", 169, 8.7f);
        restClient.post()
                .uri("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newMovie)
                .retrieve();
        System.out.println("Film nou adăugat cu succes!");


        var update = new MovieScoreUpdate("Interstellar", 9.5f);
        restClient.patch()
                .uri("/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .body(update)
                .retrieve();
        System.out.println("Scorul filmului Interstellar a fost actualizat!");

        restClient.delete()
                .uri("/movies/{title}", "Interstellar")
                .retrieve();
        System.out.println("Film șters prin cerere DELETE.");
    }
}