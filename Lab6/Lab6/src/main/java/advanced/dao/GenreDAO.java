package advanced.dao;

import advanced.database.Database;
import advanced.objects.Genre;


import java.sql.*;

public class GenreDAO {
    public Genre create(String name) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO genres(name) VALUES(?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    connection.commit();
                    return new Genre(id,name);
                } else {
                    connection.rollback();
                    throw new SQLException("Cound't registry the new genre named: "+name);
                }
            }
        }
    }

    public Genre findByName(String name) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id,name FROM genres WHERE name LIKE ?")){
            preparedStatement.setString(1,name);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? new Genre(result.getInt(1),result.getString(2)) : null;
        }
    }

    public Genre findById(int id) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id,name FROM genres WHERE id=?")){
            preparedStatement.setInt(1,id);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? new Genre(result.getInt(1),result.getString(2)) : null;
        }
    }

    public Genre getMovieGenreById(int movieId) throws SQLException{
        try(Connection connection = advanced.database.Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT g.id,g.name FROM genres g JOIN movies_genres mg ON mg.genre_id=g.id WHERE mg.movie_id=?")){
            preparedStatement.setInt(1,movieId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? new Genre(resultSet.getInt(1),resultSet.getString(2)) : null;
        }
    }

    public Genre getMovieGenreByTitle(String movieTitle) throws SQLException{
        try(Connection connection = advanced.database.Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT g.id,g.name FROM genres g JOIN movies_genres mg ON mg.genre_id=g.id JOIN movies m ON mg.movie_id=m.id WHERE m.title LIKE ?")){
            preparedStatement.setString(1,movieTitle);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() ? new Genre(resultSet.getInt(1),resultSet.getString(2)) : null;
        }
    }
}
