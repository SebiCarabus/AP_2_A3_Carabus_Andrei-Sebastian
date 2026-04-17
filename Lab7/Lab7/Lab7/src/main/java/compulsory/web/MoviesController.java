package compulsory.web;

import compulsory.DTO.ActorDTO;
import compulsory.DTO.MovieDTO;
import compulsory.domain.Actor;
import compulsory.domain.Movie;
import compulsory.persistence.dao.MovieDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
public class MoviesController {
    @Autowired
    private MovieDAO movieDAO;

    @GetMapping
    public List<MovieDTO> getAll(){
        List<Movie> movies = movieDAO.findAll();
        List<MovieDTO> moviesDTO = new ArrayList<MovieDTO>();
        for(Movie movie : movies){
            moviesDTO.add(new MovieDTO(movie));
        }
        return moviesDTO;
    }
}
