package homework.persistence.dao;

import homework.domain.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository
public class ActorDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public Actor create(String name){
        String sql = "INSERT INTO actors(name) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection ->{
            PreparedStatement preparedStatement=connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,name);
            return preparedStatement;
        },keyHolder);

        int id=(int) keyHolder.getKeys().get("id");
        return new Actor(id,name);
    }

    public List<Actor> findAll(){
        String sql = "SELECT id,name FROM actors";
        try{
            return jdbcTemplate.query(sql,(restultSet,rowNum)->new Actor(
                restultSet.getInt(1),
                restultSet.getString(2)
            ));
        }catch (Exception exception){
            return null;
        }
    }

    public Actor findByName(String name){
        String sql = "SELECT id,name FROM actors WHERE name LIKE ?";
        try {
            return jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> new Actor(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            ), name);
        }catch (Exception exception){
            return null;
        }
    }

    public Actor findById(int id) throws SQLException{
        String sql="SELECT id,name FROM actors WHERE id=?";
        try{
            return jdbcTemplate.queryForObject(sql,(resultSet, rowNum) -> new Actor(
                resultSet.getInt(1),
                resultSet.getString(2)
            ),id);
        }catch (Exception exception){
            return null;
        }
    }

    @Transactional
    public void deleteById(int id){
        String sql="DELETE FROM actors WHERE id=?";
        jdbcTemplate.update(sql,id);
    }

    @Transactional
    public void deleteByName(String name){
        String sql="DELETE FROM actors WHERE name=?";
        jdbcTemplate.update(sql,name);
    }

    @Transactional
    public  void updateName(Actor actor, String name){
        String sql = "UPDATE actors SET name=? WHERE id=?;";
        jdbcTemplate.update(sql,name,actor.getId());
    }

    @Transactional
    public List<Actor> getMovieActorsById(int movieId) throws SQLException{
        String sql="SELECT a.id,a.name FROM actors a JOIN movies_actors ma ON a.id=ma.actor_id WHERE ma.movie_id=?";
        try {
            return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Actor(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            ), movieId);
        }catch (Exception exception){
            return null;
        }
    }

    @Transactional
    public List<Actor> getMovieActorsByTitle(String movieTitle) throws SQLException{
        String sql="SELECT a.id,a.name FROM actors a JOIN movies_actors ma ON a.id=ma.actor_id JOIN movies m ON ma.movie_id=m.id WHERE m.title LIKE ?";
        try {
            return jdbcTemplate.query(sql, (resultSet, rowNum) -> new Actor(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            ), movieTitle);
        }catch (Exception exception){
            return null;
        }
    }
}
