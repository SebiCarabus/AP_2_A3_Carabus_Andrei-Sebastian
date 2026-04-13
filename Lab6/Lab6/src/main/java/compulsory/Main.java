package compulsory;

import compulsory.dao.GenreDAO;
import compulsory.database.Database;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try{
            Database.initaliseDatabase();
            var genreDAO=new GenreDAO();
            genreDAO.create("Action");
            genreDAO.create("Adventure");

            System.out.println("At id 2 in the genres table we have: "+genreDAO.findById(2));
            System.out.println("Action genre is registered at the index: "+genreDAO.findByName("Action")+" in the genres table");

            Database.dropDatabase();
            Database.closeConnection();
        } catch (SQLException exception){
            System.err.println("SQL Error: "+exception);
        }
    }

}