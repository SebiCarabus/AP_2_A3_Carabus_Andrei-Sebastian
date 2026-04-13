package homework.dao;

import homework.database.Database;
import homework.objects.Actor;

import java.sql.*;

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
}
