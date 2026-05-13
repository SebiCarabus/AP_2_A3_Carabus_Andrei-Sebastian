package compulsory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GameClient {
    private volatile boolean running=true;

    public GameClient() throws IOException{
        String serverAddress = "127.0.0.1";
        int PORT = 8100;
        try (Socket socket = new Socket(serverAddress, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream())) ) {

            while(running){
                System.out.print("command> ");
                Scanner scanner =new Scanner(System.in);
                String request = scanner.nextLine();
                if(request.isEmpty()){
                    continue;
                }

                if("exit".equals(request) || "quit".equals(request)){
                    running=false;
                    request="quit";
                }
                out.println(request);

                String response = in.readLine();
                System.out.println(response);
            }
        } catch (UnknownHostException exception) {
            System.err.println("No server listening... " + exception);
        }

    }
}
