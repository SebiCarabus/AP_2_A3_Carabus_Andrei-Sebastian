package homework.dao;

import homework.database.Database;
import homework.objects.Actor;
import homework.objects.Genre;
import homework.objects.Movie;
import homework.report.MovieReportRow;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    public Movie create(String title) throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO movies(title) VALUES(?);", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,title);
            preparedStatement.executeUpdate();

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    connection.commit();
                    return new Movie(id,title);
                } else {
                    connection.rollback();
                    throw new SQLException("Cound't registry the new movie titled: "+title);
                }
            }
        }
    }

    public Movie findByTitle(String name) throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id,title,release_date,duration,score FROM movies WHERE title LIKE ?;")){
            preparedStatement.setString(1,name);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                int id=result.getInt(1);
                String title=result.getString(2);
                var release_date=result.getDate(3);
                int duration=result.getInt(4);
                float score=result.getFloat(5);

                return new Movie(id,title,release_date,duration,score);
            }
            return null;
        }
    }

    public Movie findById(int id) throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id,title,release_date,duration,score FROM movies WHERE id=?;")){
            preparedStatement.setInt(1,id);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                String title=result.getString(2);
                var releaseDate=result.getDate(3);
                int duration=result.getInt(4);
                float score=result.getFloat(5);

                return new Movie(id,title,releaseDate,duration,score);
            }
            return null;
        }
    }

    public void addActor(Movie movie, Actor actor) throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO movies_actors VALUES (?,?);")){
            preparedStatement.setInt(1,movie.getId());
            preparedStatement.setInt(2,actor.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public void addGenre(Movie movie, Genre genre)throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO movies_genres VALUES (?,?);")){
            preparedStatement.setInt(1,movie.getId());
            preparedStatement.setInt(2,genre.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public  void updateTtile(Movie movie,String title)throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE movies SET title=? WHERE id=?;")){
            preparedStatement.setString(1,title);
            preparedStatement.setInt(2,movie.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public  void updateReleaseDate(Movie movie,Date releaseDate)throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE movies SET release_date=? WHERE id=?;")){
            preparedStatement.setDate(1,releaseDate);
            preparedStatement.setInt(2,movie.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public  void updateDuration(Movie movie,int duration)throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE movies SET duration=? WHERE id=?;")){
            preparedStatement.setInt(1,duration);
            preparedStatement.setInt(2,movie.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public  void updateScore(Movie movie,Float score)throws SQLException{
        try(Connection connection = Database.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE movies SET score=? WHERE id=?;")){
            preparedStatement.setFloat(1,score);
            preparedStatement.setInt(2,movie.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

}
