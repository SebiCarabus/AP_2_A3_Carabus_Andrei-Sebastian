package homework;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class GameClient {
    private AtomicBoolean running;

    public GameClient(AtomicBoolean isRunning) throws IOException {
        this.running = isRunning;
        String serverAddress = "127.0.0.1";
        int PORT = 8100;

        try (Socket socket = new Socket(serverAddress, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            ReaderThread readerThread = new ReaderThread(running, socket);
            readerThread.start();

            Scanner scanner = new Scanner(System.in);

            while (running.get()) {
                try {
                    if (System.in.available() > 0) {
                        String request = scanner.nextLine();
                        if (request.isEmpty()) {
                            continue;
                        }

                        if ("exit".equals(request) || "quit".equals(request)) {
                            request = "quit";
                            //running.set(false);
                        }

                        out.println(request);
                    } else {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrupted: " + exception);
                    break;
                }
            }
        } catch (UnknownHostException exception) {
            System.err.println("No server listening... " + exception);
        }
    }
}