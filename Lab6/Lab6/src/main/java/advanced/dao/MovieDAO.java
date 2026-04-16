package advanced.dao;

import advanced.database.Database;
import advanced.objects.Actor;

import advanced.objects.Genre;
import advanced.objects.Movie;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MovieDAO {
    public Movie create(int id,String title) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO movies(id,title) VALUES(?,?);")){
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,title);
            preparedStatement.executeUpdate();
            return new Movie(id,title);
        }
    }

    public Movie findByTitle(String name) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id,title,release_date,duration,score FROM movies WHERE title LIKE ?;")){
            preparedStatement.setString(1,name);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                int id=result.getInt(1);
                String title=result.getString(2);
                var releaseDate=result.getDate(3);
                int duration=result.getInt(4);
                float score=result.getFloat(5);
                List<Actor> actors=new ArrayList<Actor>();
                ActorDAO actorDAO = new ActorDAO();
                actors=actorDAO.getMovieActorsById(result.getInt(1));
                Genre genre = null;
                GenreDAO genreDAO = new GenreDAO();
                genre = genreDAO.getMovieGenreById(result.getInt(1));
                return new Movie(id,title,releaseDate,duration,score,genre,actors);
            }
            return null;
        }
    }

    public Movie findById(int id) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT id,title,release_date,duration,score FROM movies WHERE id=?;")){
            preparedStatement.setInt(1,id);
            ResultSet result = preparedStatement.executeQuery();
            if(result.next()){
                String title=result.getString(2);
                var releaseDate=result.getDate(3);
                int duration=result.getInt(4);
                float score=result.getFloat(5);
                List<Actor> actors=new ArrayList<Actor>();
                ActorDAO actorDAO = new ActorDAO();
                actors=actorDAO.getMovieActorsById(result.getInt(1));
                Genre genre = null;
                GenreDAO genreDAO = new GenreDAO();
                genre = genreDAO.getMovieGenreById(result.getInt(1));
                return new Movie(id,title,releaseDate,duration,score,genre,actors);
            }
            return null;
        }
    }

    public void addActor(Movie movie, Actor actor) throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO movies_actors VALUES (?,?);")){
            preparedStatement.setInt(1,movie.getId());
            preparedStatement.setInt(2,actor.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public void addGenre(Movie movie, Genre genre)throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("INSERT INTO movies_genres VALUES (?,?);")){
            preparedStatement.setInt(1,movie.getId());
            preparedStatement.setInt(2,genre.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public  void updateTtile(Movie movie, String title)throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE movies SET title=? WHERE id=?;")){
            preparedStatement.setString(1,title);
            preparedStatement.setInt(2,movie.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public  void updateReleaseDate(Movie movie, Date releaseDate)throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE movies SET release_date=? WHERE id=?;")){
            preparedStatement.setDate(1,releaseDate);
            preparedStatement.setInt(2,movie.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public  void updateDuration(Movie movie, int duration)throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE movies SET duration=? WHERE id=?;")){
            preparedStatement.setInt(1,duration);
            preparedStatement.setInt(2,movie.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public  void updateScore(Movie movie, Float score)throws SQLException{
        try(Connection connection = Database.getInstance().getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("UPDATE movies SET score=? WHERE id=?;")){
            preparedStatement.setFloat(1,score);
            preparedStatement.setInt(2,movie.getId());
            preparedStatement.executeUpdate();
            connection.commit();
        }
    }

    public Map<Integer, Set<Integer>> getMovieAdjacencyList() throws SQLException {
        Map<Integer, Set<Integer>> adjacency = new HashMap<>();

        String sql = "SELECT ma1.movie_id, ma2.movie_id " +
                "FROM movies_actors ma1 " +
                "JOIN movies_actors ma2 ON ma1.actor_id = ma2.actor_id " +
                "WHERE ma1.movie_id != ma2.movie_id";

        try (Connection connection = Database.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                int idMovie1 = resultSet.getInt(1);
                int idMovie2 = resultSet.getInt(2);
                adjacency.computeIfAbsent(idMovie1, adjacencyList -> new HashSet<>()).add(idMovie2);
                adjacency.computeIfAbsent(idMovie2, adjacencyList -> new HashSet<>()).add(idMovie1);
            }
        }
        return adjacency;
    }

    public List<Movie> findAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT id, title, release_date, duration, score FROM movies";
        try (Connection connection = Database.getInstance().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                List<Actor> actors=new ArrayList<Actor>();
                ActorDAO actorDAO = new ActorDAO();
                actors=actorDAO.getMovieActorsById(resultSet.getInt(1));
                Genre genre = null;
                GenreDAO genreDAO = new GenreDAO();
                genre = genreDAO.getMovieGenreById(resultSet.getInt(1));
                movies.add(new Movie(resultSet.getInt(1), resultSet.getString(2),
                        resultSet.getDate(3), resultSet.getInt(4), resultSet.getFloat(5),genre,actors));
            }
        }
        return movies;
    }
}
