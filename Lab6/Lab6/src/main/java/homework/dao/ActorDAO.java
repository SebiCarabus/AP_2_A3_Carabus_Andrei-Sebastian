package homework.dao;

import homework.database.Database;
import homework.objects.Actor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActorDAO {
    public Actor create(String name) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO actors(name) VALUES(?)", Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();

            try(ResultSet generatedKeys = preparedStatement.getGeneratedKeys()){
                if(generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    connection.commit();
                    return new Actor(id,name);
                } else {
                    connection.rollback();
                    throw new SQLException("Cound't registry the new actor named: "+name);
                }
            }
        }
    }

    public Actor findByName(String name) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id,name FROM actors WHERE name LIKE ?")){
            preparedStatement.setString(1,name);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? new Actor(result.getInt(1),result.getString(2)) : null;
        }
    }

    public Actor findById(int id) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id,name FROM actors WHERE id=?")){
            preparedStatement.setInt(1,id);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? new Actor(result.getInt(1),result.getString(2)) : null;
        }
    }

    public List<Actor> getMovieActorsById(int movieId) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT a.id,a.name FROM actors a JOIN movies_actors ma ON a.id=ma.actor_id WHERE ma.movie_id=?")){
            preparedStatement.setInt(1,movieId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Actor> actors = new ArrayList<Actor>();
            while (resultSet.next()) {
                actors.add(new Actor(resultSet.getInt(1), resultSet.getString(2)));
            }
            return actors;
        }
    }

    public List<Actor> getMovieActorsByTitle(String movieTitle) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT a.id,a.name FROM actors a JOIN movies_actors ma ON a.id=ma.actor_id JOIN movies m ON ma.movie_id=m.id WHERE m.title LIKE ?")){
            preparedStatement.setString(1,movieTitle);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Actor> actors = new ArrayList<Actor>();
            while (resultSet.next()) {
                actors.add(new Actor(resultSet.getInt(1), resultSet.getString(2)));
            }
            return actors;
        }
    }
}
