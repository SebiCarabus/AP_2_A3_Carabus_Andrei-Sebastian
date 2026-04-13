package homework;

import homework.dao.ActorDAO;
import homework.dao.GenreDAO;
import homework.dao.MovieDAO;
import homework.database.Database;
import homework.objects.Actor;
import homework.objects.Movie;
import homework.report.ReportService;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            Database.initaliseDatabase();
            var genreDAO=new GenreDAO();
            var action=genreDAO.create("Action");
            var adventure=genreDAO.create("Adventure");

            var actorDAO=new ActorDAO();
            var chuck=actorDAO.create("Chuck Norris");

            var movieDAO=new MovieDAO();
            var movie=movieDAO.create("Walker, Texas Ranger");
            movie.addGenre(action);
            movie.setDuration(200);
            movie.setScore((float)9.2);

            var reportService= new ReportService();
            reportService.generateAndOpenReport(Database.getMoviesForReport());

            Database.dropDatabase();
        } catch (SQLException exception){
            System.err.println("SQL Error: "+exception);
        }
    }

}