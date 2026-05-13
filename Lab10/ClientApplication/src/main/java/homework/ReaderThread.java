package homework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
@AllArgsConstructor
public class ReaderThread extends Thread {
    private AtomicBoolean running;
    private final Socket socket;

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            while (running.get()) {
                String response = in.readLine();

                if (response == null) {
                    running.set(false);
                    break;
                }

                if ("KILL".equals(response)) {
                    running.set(false);
                    //System.exit(0);
                    break;
                }
                System.out.println(response);
            }
        } catch (UnknownHostException exception) {
            System.err.println("No server listening... " + exception);
        } catch (IOException exception) {
            if(running.get()) {
                System.err.println("Something went wrong receiving message from the server... " + exception);
            }
        }
    }
}