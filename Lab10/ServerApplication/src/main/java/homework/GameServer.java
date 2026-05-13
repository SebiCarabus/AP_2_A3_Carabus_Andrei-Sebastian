package homework;

import homework.game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class GameServer {
    private static final int PORT = 8100;
    private volatile boolean running=true;
    private Game game= new Game(2);
    public GameServer(){
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try(ServerSocket serverSocket = new ServerSocket(PORT);){
            while(running){
                Socket socket = serverSocket.accept();
                pool.execute(new ClientThread(socket,game));
            }
        } catch (IOException exception){
            System.err.println(exception);
        } finally{
            pool.shutdown();
        }
    }

    public static void main(String[] args) {
        GameServer gameServer =new GameServer();
    }
}