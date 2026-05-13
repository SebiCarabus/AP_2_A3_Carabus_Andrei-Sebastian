package compulsory;


import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try{
            GameClient gameClient= new GameClient();
        } catch (IOException exception){
            System.err.println("Something went wrong: "+exception);
        }

    }
}