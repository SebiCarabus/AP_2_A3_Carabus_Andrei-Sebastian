package homework.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import homework.report.MovieReportRow;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/javaLab6";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";
    private static HikariDataSource dataSource=null;

    private Database(){}

    public static void setConfiguration(){
        HikariConfig configuration = new HikariConfig();
        configuration.setJdbcUrl(URL);
        configuration.setUsername(USER);
        configuration.setPassword(PASSWORD);
        configuration.setAutoCommit(false);
        configuration.setMaximumPoolSize(10);
        configuration.setConnectionTimeout(30000);

        dataSource=new HikariDataSource(configuration);
    }
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    public static void closeConnection() throws SQLException {
        dataSource.close();
    }
    public static void initaliseDatabase() throws SQLException{
        String sql = new StringBuilder()
                .append("CREATE TABLE IF NOT EXISTS genres(id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL UNIQUE);")
                .append("CREATE TABLE IF NOT EXISTS actors(id SERIAL PRIMARY KEY, name VARCHAR(50) NOT NULL UNIQUE);")
                .append("CREATE TABLE IF NOT EXISTS movies(id SERIAL PRIMARY KEY, title VARCHAR(50) NOT NULL UNIQUE, release_date DATE," +
                        "duration INTEGER, score DECIMAL(2,1));")
                .append("CREATE TABLE IF NOT EXISTS movies_actors(movie_id INTEGER REFERENCES movies(id),actor_id INTEGER REFERENCES actors(id));")
                .append("CREATE TABLE IF NOT EXISTS movies_genres(movie_id INTEGER REFERENCES movies(id) UNIQUE,genre_id INTEGER REFERENCES genres(id));")
                .append("CREATE OR REPLACE VIEW movie_report_view AS " +
                        "SELECT " +
                        "    m.id, " +
                        "    m.title, " +
                        "    m.release_date," +
                        "    m.duration, " +
                        "    m.score, " +
                        "    g.name AS genre_name" +
                        " FROM movies m " +
                        " LEFT JOIN movies_genres mg ON m.id = mg.movie_id " +
                        " LEFT JOIN genres g ON mg.genre_id = g.id; ")
                .toString();
        try(Connection connection = Database.getConnection()){
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            connection.commit();
        }
    }

    public static void dropDatabase() throws SQLException{
        String sql = new StringBuilder()
                .append("DROP VIEW IF EXISTS movie_report_view;")
                .append("DROP TABLE IF EXISTS movies_actors;")
                .append("DROP TABLE IF EXISTS movies_genres;")
                .append("DROP TABLE IF EXISTS genres;")
                .append("DROP TABLE IF EXISTS actors;")
                .append("DROP TABLE IF EXISTS movies;")
                .toString();
        try(Connection connection = Database.getConnection()){
            Statement statement=connection.createStatement();
            statement.executeUpdate(sql);
            connection.commit();
        }
    }

    public static List<MovieReportRow> getMoviesForReport() throws SQLException {
        List<MovieReportRow> report = new ArrayList<>();

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id,title,release_date,duration,score,genre_name FROM movie_report_view;");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                report.add(new MovieReportRow(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getDate("release_date"),
                        resultSet.getInt("duration"),
                        resultSet.getFloat("score"),
                        resultSet.getString("genre_name")
                ));
            }
        }
        return report;
    }
}
