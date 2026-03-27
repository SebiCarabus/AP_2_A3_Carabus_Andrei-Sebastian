package compulsory.dao;

import compulsory.database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreDAO {
    public void create(String name) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO genres(name) VALUES(?)")){
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public Integer findByName(String name) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id FROM genres WHERE name LIKE ?")){
            preparedStatement.setString(1,name);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getInt(1) : null;
        }
    }

    public String findById(int id) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT name FROM genres WHERE id=?")){
            preparedStatement.setInt(1,id);
            ResultSet result = preparedStatement.executeQuery();
            return result.next() ? result.getString(1) : null;
        }
    }
}
