package advanced.objects;

import advanced.dao.MovieDAO;
import lombok.Getter;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Getter
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
        MovieDAO movieDAO=new MovieDAO();
        movieDAO.addGenre(this,genre);
        this.genre = genre;
    }

    public void addActor(Actor actor) throws SQLException {
        MovieDAO movieDAO=new MovieDAO();
        movieDAO.addActor(this,actor);
        this.actors.add(actor);
    }

    public void setTitle(String title) throws SQLException{
        MovieDAO movieDAO=new MovieDAO();
        movieDAO.updateTtile(this,title);
        this.title = title;
    }

    public void setReleaseDate(Date releaseDate) throws SQLException{
        MovieDAO movieDAO=new MovieDAO();
        movieDAO.updateReleaseDate(this,releaseDate);
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) throws SQLException{
        MovieDAO movieDAO=new MovieDAO();
        movieDAO.updateDuration(this,duration);
        this.duration = duration;
    }

    public void setScore(float score)throws SQLException {
        MovieDAO movieDAO=new MovieDAO();
        movieDAO.updateScore(this,score);
        this.score = score;
    }
}
