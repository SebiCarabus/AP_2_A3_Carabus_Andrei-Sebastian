package ServerApplication;

import ServerApplication.compulsory.ClientThread;
import ServerApplication.compulsory.Game;
import ServerApplication.compulsory.repsoitories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

	private final QuestionRepository questionRepository;

	private static final int PORT = 8100;
	private volatile boolean running=true;
	private Game game;

	public ServerApplication(QuestionRepository questionRepository){
		this.questionRepository=questionRepository;
	}
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.game=new Game(2,this.questionRepository);
		System.out.println("We start the Server Socket on port 8100");
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

}
