package compulsory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private volatile boolean listening = true;

    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try (socket;
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (listening) {
                String request = in.readLine();
                String response = "Server received the request ... ";
                if ("quit".equals(request)) {
                    response = "Server stopped";
                    listening = false;
                }
                out.println(response);
                out.flush();
            }
        } catch (IOException exception) {
            System.err.println("Communication error... " + exception);
        }
    }
}