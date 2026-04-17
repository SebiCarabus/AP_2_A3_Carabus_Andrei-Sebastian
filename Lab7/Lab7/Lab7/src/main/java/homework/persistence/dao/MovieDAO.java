package homework.persistence.dao;

import homework.domain.Actor;
import homework.domain.Genre;
import homework.domain.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class MovieDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ActorDAO actorDAO;
    @Autowired
    private GenreDAO genreDAO;

    @Transactional
    public Movie create(int id, String title){
        String sql ="INSERT INTO movies(id, title) VALUES(?, ?);";
        jdbcTemplate.update(sql, id, title);
        return new Movie(id, title);
    }

    @Transactional
    public Movie createWithoutId(String title){
        String sql ="INSERT INTO movies(id,title) VALUES((SELECT MAX(id)+1 FROM movies),?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection ->{
            PreparedStatement preparedStatement=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,title);
            return preparedStatement;
        },keyHolder);

        int id=(int) keyHolder.getKeys().get("id");
        return new Movie(id,title);
    }

    public Movie findByTitle(String name){
        String sql="SELECT id,title,release_date,duration,score FROM movies WHERE title LIKE ?;";
        try {
            List<Actor> actors=actorDAO.getMovieActorsByTitle(name);
            Genre genre = genreDAO.getMovieGenreByTitle(name);
            return jdbcTemplate.queryForObject(sql,(resultSet,rowNum)->new Movie(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getDate(3),
                    resultSet.getInt(4),
                    resultSet.getFloat(5),
                    genre,
                    actors),name);
        } catch (Exception exception){
        return null;
        }
    }

    public Movie findById(int id){
        String sql="SELECT id,title,release_date,duration,score FROM movies WHERE id=?;";
        try{
            List<Actor> actors=actorDAO.getMovieActorsById(id);
            Genre genre = genreDAO.getMovieGenreById(id);
            return jdbcTemplate.queryForObject(sql,(resultSet,rowNum)->new Movie(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getDate(3),
                resultSet.getInt(4),
                resultSet.getFloat(5),
                genre,
                actors),id);
        } catch (Exception exception){
            return null;
        }
    }

    @Transactional
    public void addActor(Movie movie, Actor actor){
        String sql="INSERT INTO movies_actors VALUES (?,?);";
        jdbcTemplate.update(sql,movie.getId(),actor.getId());
    }

    @Transactional
    public void addGenre(Movie movie, Genre genre){
        String sql="INSERT INTO movies_genres VALUES (?,?);";
        jdbcTemplate.update(sql,movie.getId(),genre.getId());
    }

    @Transactional
    public  void updateTtile(Movie movie, String title){
        String sql="UPDATE movies SET title=? WHERE id=?;";
        jdbcTemplate.update(sql,title,movie.getId());

    }

    @Transactional
    public void updateReleaseDate(Movie movie, Date releaseDate){
        String sql = "UPDATE movies SET release_date=? WHERE id=?;";
        jdbcTemplate.update(sql,releaseDate,movie.getId());
    }

    @Transactional
    public  void updateDuration(Movie movie, int duration){
        String sql = "UPDATE movies SET duration=? WHERE id=?;";
        jdbcTemplate.update(sql,duration,movie.getId());
    }

    @Transactional
    public  void updateScore(Movie movie, Float score){
        String sql="UPDATE movies SET score=? WHERE id=?;";
        jdbcTemplate.update(sql,score,movie.getId());
    }

    public Map<Integer, Set<Integer>> getMovieAdjacencyList(){
        Map<Integer, Set<Integer>> adjacency = new HashMap<>();
        String sql = "SELECT ma1.movie_id, ma2.movie_id " +
                "FROM movies_actors ma1 " +
                "JOIN movies_actors ma2 ON ma1.actor_id = ma2.actor_id " +
                "WHERE ma1.movie_id != ma2.movie_id";

        jdbcTemplate.query(sql, (rs) -> {
            int idMovie1 = rs.getInt(1);
            int idMovie2 = rs.getInt(2);
            adjacency.computeIfAbsent(idMovie1, k -> new HashSet<>()).add(idMovie2);
            adjacency.computeIfAbsent(idMovie2, k -> new HashSet<>()).add(idMovie1);
        });
        return adjacency;
    }

    public List<Movie> findAll(){
        String sql = "SELECT id, title, release_date, duration, score FROM movies";
        return jdbcTemplate.query(sql,(resultSet,rowNum)->{
            List<Actor> actors=actorDAO.getMovieActorsById(resultSet.getInt("id"));
            Genre genre = genreDAO.getMovieGenreById(resultSet.getInt("id"));
            return new Movie(
                    resultSet.getInt("id"),
                    resultSet.getString("title"),
                    resultSet.getDate(3),
                    resultSet.getInt(4),
                    resultSet.getFloat(5),
                    genre,
                    actors
            );
        });
    }

    @Transactional
    public void deleteById(int id){
        String sql="DELETE FROM movies WHERE id=?";
        jdbcTemplate.update(sql,id);
    }

    @Transactional
    public void deleteByTitle(String name){
        String sql="DELETE FROM movies WHERE title=?";
        jdbcTemplate.update(sql,name);
    }
}
