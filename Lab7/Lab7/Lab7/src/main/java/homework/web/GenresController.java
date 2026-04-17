package homework.web;

import homework.DTO.GenreDTO;
import homework.domain.Genre;
import homework.persistence.dao.GenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/genres")
public class GenresController {
    @Autowired
    private GenreDAO genreDAO;

    @GetMapping
    public List<GenreDTO> getAll(){
        List<Genre> genres = genreDAO.findAll();
        List<GenreDTO> genresDTO = new ArrayList<GenreDTO>();
        genres.forEach(genre -> {
            genresDTO.add(new GenreDTO((genre)));
        });
        return genresDTO;
    }
}
