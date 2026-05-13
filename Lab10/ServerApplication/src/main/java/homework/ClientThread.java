package homework;

import homework.game.Game;
import homework.game.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private volatile boolean listening = true;
    private PrintWriter out;
    private Game game;
    private Player player;

    public ClientThread(Socket socket, Game game) {
        this.socket = socket;
        this.game = game;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void run() {
        try (socket;
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            this.out = out;

            this.player = new Player("Player-" + socket.getPort(), this);
            game.addPlayer(this.player);

            while (listening) {
                String request = in.readLine();

                if (game.isInProgress()) {
                    game.submitAnswer(player, request);
                } else {
                    if (request==null || "quit".equals(request)) {
                        listening = false;
                        sendMessage("Server Stopped");
                        this.player.getClientThread().sendMessage("KILL");
                        this.game.getPlayers().remove(this.player);
                        break;
                    }
                    sendMessage("We are wating for other players...");
                }
            }
        } catch (IOException exception) {
            System.err.println("Communication error... " + exception);
        }
    }
}