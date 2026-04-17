package advanced.web;

import advanced.DTO.MovieDTO;
import advanced.domain.Actor;
import advanced.domain.Genre;
import advanced.domain.Movie;
import advanced.persistence.dao.ActorDAO;
import advanced.persistence.dao.GenreDAO;
import advanced.persistence.dao.MovieDAO;
import advanced.services.MovieContraintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/movies")
public class MoviesController {
    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private GenreDAO genreDAO;

    @Autowired
    private ActorDAO actorDAO;

    @Autowired
    private MovieContraintService movieContraintService;

    @GetMapping
    public List<MovieDTO> getAll(){
        List<Movie> movies = movieDAO.findAll();
        List<MovieDTO> moviesDTO = new ArrayList<MovieDTO>();
        for(Movie movie : movies){
            moviesDTO.add(new MovieDTO(movie));
        }
        return moviesDTO;
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<String> deleteMovieByTitle(@PathVariable("title") String movieTitle){
        Movie movie = movieDAO.findByTitle(movieTitle);
        if(movie==null){
            return ResponseEntity
                    .notFound()
                    .build();
        }

        movieDAO.deleteByTitle(movieTitle);

        return ResponseEntity
                .ok()
                .body("Successfully deleted the movie with the title \""+movieTitle+"\"");
    }

    @PostMapping
    public ResponseEntity<String> addNewMovie(@RequestBody MovieDTO newMovieDTO){
        if(movieDAO.findByTitle(newMovieDTO.getTitle())!=null){
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        Movie newMovie=movieDAO.createWithoutId(newMovieDTO.getTitle());
        movieDAO.updateReleaseDate(newMovie,newMovieDTO.getReleaseDate());
        movieDAO.updateDuration(newMovie,newMovieDTO.getDuration());
        movieDAO.updateScore(newMovie,newMovieDTO.getScore());

        if(newMovieDTO.getGenre()!=null){
            Genre newMovieGenre=genreDAO.findByName(newMovieDTO.getGenre());
            if(newMovieGenre==null){
                newMovieGenre=genreDAO.create(newMovieDTO.getGenre());
            }
            movieDAO.addGenre(newMovie,newMovieGenre);
        }

        for(String actor : newMovieDTO.getActors()){
            Actor newMovieActor=actorDAO.findByName(actor);
            if(newMovieActor==null){
                newMovieActor=actorDAO.create(actor);
            }
            movieDAO.addActor(newMovie,newMovieActor);
        }

        return ResponseEntity
                .status(200)
                .body("The new movie was put with success!");
    }

    public record MovieScoreUpdate(String title, float score) {}
    @PatchMapping
    public ResponseEntity<String> modifyMovieScore(@RequestBody MovieScoreUpdate update){
        Movie movie=movieDAO.findByTitle(update.title);
        if(movie==null){
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        movieDAO.updateScore(movie, update.score());

        return ResponseEntity
                .status(200)
                .body("The score of the movie was updated with success!");
    }

    @PutMapping
    public ResponseEntity<String> updateMovie(@RequestBody MovieDTO newMovieDTO){
        Movie currentMovie=movieDAO.findByTitle(newMovieDTO.getTitle());
        if(currentMovie==null){
            return ResponseEntity
                    .badRequest()
                    .build();
        }
        int id=currentMovie.getId();
        movieDAO.deleteById(id);
        Movie newMovie=movieDAO.create(id,newMovieDTO.getTitle());
        movieDAO.updateReleaseDate(newMovie,newMovieDTO.getReleaseDate());
        movieDAO.updateDuration(newMovie,newMovieDTO.getDuration());
        movieDAO.updateScore(newMovie,newMovieDTO.getScore());

        if(newMovieDTO.getGenre()!=null){
            Genre newMovieGenre=genreDAO.findByName(newMovieDTO.getGenre());
            if(newMovieGenre==null){
                newMovieGenre=genreDAO.create(newMovieDTO.getGenre());
            }
            movieDAO.addGenre(newMovie,newMovieGenre);
        }

        for(String actor : newMovieDTO.getActors()){
            Actor newMovieActor=actorDAO.findByName(actor);
            if(newMovieActor==null){
                newMovieActor=actorDAO.create(actor);
            }
            movieDAO.addActor(newMovie,newMovieActor);
        }

        return ResponseEntity
                .status(200)
                .body("The movie was updated with success!");
    }

    @GetMapping("/unrelated/{min}")
    public ResponseEntity<?> getUnrelatedMovies(@PathVariable("min") int minSize){
        List<Movie> allMovies=movieDAO.findAll();
        Map<Integer, Set<Integer>> adjacency = movieDAO.getMovieAdjacencyList();

        List<Movie> result = movieContraintService.findUnrelatedMovies(allMovies,adjacency,minSize);

        if(result.isEmpty()){
            return ResponseEntity
                    .status(404)
                    .body("There couldn't be find a list of unrelated movies of the length "+minSize);
        }

        return ResponseEntity.ok(result.stream().map(movie->{return new MovieDTO(movie);}).collect(Collectors.toList()));
    }

}
