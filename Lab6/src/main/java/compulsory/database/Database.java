package compulsory.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/javaLab6";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";
    private static Connection connection = null;

    private static Database database = null;
    private Database(){}

    public Connection getConnection() throws SQLException {
        if(connection==null||connection.isClosed()){
            createConnection();
        }
        return connection;
        /// asta ete naive sigleton
    }

    public static Database getInstance(){
        if(database==null){
            database = new Database();
        }
        return database;
        /// asta este cu adevarat sigleton
    }

    private static void createConnection()throws SQLException{
        connection = DriverManager.getConnection(URL,USER,PASSWORD);
        connection.setAutoCommit(false);
    }

    public static void closeConnection()throws SQLException{
        if(connection!=null&&!connection.isClosed()){
            connection.close();
        }
    }

    public static void initaliseDatabase() throws SQLException{
        String sql = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS genres(id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL UNIQUE);")
                .append("CREATE TABLE IF NOT EXISTS actors(id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL UNIQUE);")
                .append("CREATE TABLE IF NOT EXISTS movies(id SERIAL PRIMARY KEY, title VARCHAR(50) NOT NULL UNIQUE, release_date DATE," +
                        " duration INTEGER, score DECIMAL(2,1));")
                .append("CREATE TABLE IF NOT EXISTS movies_actors(movie_id INTEGER REFERENCES movies(id),actor_id INTEGER REFERENCES actors(id));")
                .append("CREATE TABLE IF NOT EXISTS movies_genres(movie_id INTEGER REFERENCES movies(id) UNIQUE,genre_id INTEGER REFERENCES genres(id));")
                .toString();
        try(Connection connection = Database.getInstance().getConnection()){
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            connection.commit();
        }
    }

    public static void dropDatabase() throws SQLException{
        String sql = new StringBuilder()
                .append("DROP TABLE IF EXISTS movies_actors;")
                .append("DROP TABLE IF EXISTS movies_genres;")
                .append("DROP TABLE IF EXISTS genres;")
                .append("DROP TABLE IF EXISTS actors;")
                .append("DROP TABLE IF EXISTS movies;")
                .toString();
        try(Connection connection = Database.getInstance().getConnection()){
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            connection.commit();
        }
    }
}
