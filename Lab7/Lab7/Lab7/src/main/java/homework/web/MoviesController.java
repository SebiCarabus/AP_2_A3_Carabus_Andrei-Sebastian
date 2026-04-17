package homework.web;

import homework.DTO.MovieDTO;
import homework.domain.Actor;
import homework.domain.Genre;
import homework.domain.Movie;
import homework.persistence.dao.ActorDAO;
import homework.persistence.dao.GenreDAO;
import homework.persistence.dao.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MoviesController {
    @Autowired
    private MovieDAO movieDAO;

    @Autowired
    private GenreDAO genreDAO;

    @Autowired
    private ActorDAO actorDAO;

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
}
