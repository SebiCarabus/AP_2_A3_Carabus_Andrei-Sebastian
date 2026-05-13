package homework;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {

    public static void main(String[] args) {
        try{
            AtomicBoolean running = new AtomicBoolean(true);
            GameClient gameClient= new GameClient(running);
        } catch (IOException exception){
            System.err.println("Something went wrong: "+exception);
        }

    }
}