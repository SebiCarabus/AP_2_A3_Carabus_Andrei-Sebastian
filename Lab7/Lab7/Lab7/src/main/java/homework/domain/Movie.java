package homework.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
public class Movie {
    private int id;
    private String title;
    private Date releaseDate;
    private int duration;
    private float score;
    private Genre genre;
    private List<Actor> actors = new ArrayList<>();

    public Movie(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Movie(int id, String title, Date release_date, int duration, float score) {
        this.id = id;
        this.title = title;
        this.releaseDate = release_date;
        this.duration = duration;
        this.score = score;
    }

    public void addGenre(Genre genre) throws SQLException {
        this.genre = genre;
    }

    public void addActor(Actor actor) throws SQLException {
        this.actors.add(actor);
    }

    public void setTitle(String title) throws SQLException{
        this.title = title;
    }

    public void setReleaseDate(Date releaseDate) throws SQLException{
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) throws SQLException{
        this.duration = duration;
    }

    public void setScore(float score)throws SQLException {
        this.score = score;
    }
}
