package homework.persistence.dao;

import homework.domain.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GenreDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public Genre create(String name){
        String sql = "INSERT INTO genres(name) VALUES(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection ->{
            PreparedStatement preparedStatement=connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,name);
            return preparedStatement;
        },keyHolder);

        int id=(int) keyHolder.getKeys().get("id");
        return new Genre(id,name);
    }

    public List<Genre> findAll(){
        String sql = "SELECT id,name FROM genres";
        try {
            return jdbcTemplate.query(sql, (restultSet, rowNum) -> new Genre(
                    restultSet.getInt(1),
                    restultSet.getString(2)
            ));
        } catch (Exception exception){
            return null;
        }
    }

    public Genre findByName(String name){
        String sql = "SELECT id,name FROM genres WHERE name LIKE ?";
        try{
            return jdbcTemplate.queryForObject(sql,(resultSet, rowNum) -> new Genre(
                resultSet.getInt(1),
                resultSet.getString(2)
            ),name);
        } catch (Exception exception){
        return null;
        }
    }

    public Genre getMovieGenreById(int movieId){
        String sql = "SELECT g.id,g.name FROM genres g JOIN movies_genres mg ON mg.genre_id=g.id WHERE mg.movie_id=?";
        try{
            return jdbcTemplate.queryForObject(sql,(resultSet, rowNum) -> new Genre(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            ),movieId);
        } catch (Exception exception){
            return null;
        }
    }

    public Genre getMovieGenreByTitle(String movieTitle){
        String sql = "SELECT g.id,g.name FROM genres g JOIN movies_genres mg ON mg.genre_id=g.id JOIN movies m ON mg.movie_id=m.id WHERE m.title LIKE ?";
        try{
            return jdbcTemplate.queryForObject(sql,(resultSet, rowNum) -> new Genre(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            ),movieTitle);
        } catch (Exception exception){
            return null;
        }
    }

    public Genre findById(int id){
        try{
            String sql="SELECT id,name FROM genres WHERE id=?";
            return jdbcTemplate.queryForObject(sql,(resultSet, rowNum) -> new Genre(
                    resultSet.getInt(1),
                    resultSet.getString(2)
            ),id);
        } catch (Exception exception){
            return null;
        }
    }

    @Transactional
    public void deleteById(int id){
        String sql="DELETE FROM genres WHERE id=?";
        jdbcTemplate.update(sql,id);
    }

    @Transactional
    public void deleteByName(String name){
        String sql="DELETE FROM genres WHERE name=?";
        jdbcTemplate.update(sql,name);
    }

    @Transactional
    public  void updateName(Genre genre, String name){
        String sql = "UPDATE genres SET name=? WHERE id=?;";
        jdbcTemplate.update(sql,name,genre.getId());
    }
}
