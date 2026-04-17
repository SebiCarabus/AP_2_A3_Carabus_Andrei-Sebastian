package compulsory.DTO;

import compulsory.domain.Actor;
import compulsory.domain.Genre;
import compulsory.domain.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class MovieDTO {
    //private int id;
    private String title;
    private Date releaseDate;
    private int duration;
    private float score;
    private String genre;
    private List<String> actors = new ArrayList<>();

    public MovieDTO(Movie movie){
        //this.id= movie.getId();
        this.title=movie.getTitle();
        this.releaseDate=movie.getReleaseDate();
        this.duration= movie.getDuration();
        this.score=movie.getScore();
        this.genre=movie.getGenre().getName();
        List<Actor> actorsRaw=movie.getActors();
        for(Actor actor : actorsRaw){
            this.actors.add(actor.getName());
        }
    }
}
