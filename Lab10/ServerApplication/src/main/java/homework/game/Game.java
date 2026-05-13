package homework.game;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
public class Game implements Runnable{
    private final List<Player> players =new ArrayList<>();
    private final List<Question> questions =new ArrayList<>();
    private boolean inProgress = false;
    private int nrMaxPlayers;

    private int answersReceivedThisRound = 0;
    private long currentQuestionStartTime = 0;
    private Question currentQuestion;
    private Map<Player,Boolean> currentPlayersAnswered=new HashMap<>();

    public Game(int nrMaxPlayers){
        loadQuestions("questions.txt");
        this.nrMaxPlayers=nrMaxPlayers;
    }

    private void loadQuestions(String filePath){
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(filePath))){
            String line;
            while((line=bufferedReader.readLine())!=null){
                String[] parts = line.split(";");

                if(parts.length==6){
                    String text=parts[0];
                    List<String> options= Arrays.asList(parts[1],parts[2],parts[3],parts[4]);
                    int correctOption=Integer.parseInt(parts[5]);

                    questions.add(new Question(text,options,correctOption));
                }
            }
        } catch (IOException | NumberFormatException exception){
            System.err.println("Error processing loading the questions: "+exception);
        }
    }

    public synchronized void addPlayer(Player player){
        if(!inProgress){
            players.add(player);
            if (this.players.size()>=this.nrMaxPlayers){
                inProgress=true;
                Thread gameThread=new Thread(this);
                gameThread.start();
            }
        } else {
            player.getClientThread().sendMessage("The game already started...");
        }
    }

    public synchronized  void submitAnswer(Player player,String answer){
        if(!inProgress || currentQuestion==null){
            return;
        }

        if (Boolean.FALSE.equals(currentPlayersAnswered.get(player))){

            long responseTime = System.currentTimeMillis() - currentQuestionStartTime;
            player.addResponseTime(responseTime);

            try{
                int answerIndex = Integer.parseInt(answer.trim());

                if(answerIndex==currentQuestion.getCorrectOptionIndex()){
                    player.incrementScore();
                    player.getClientThread().sendMessage("Correct answer! Time: "+responseTime);
                } else{
                    player.getClientThread().sendMessage("Wrong answer");
                }
            } catch (NumberFormatException exception){
                player.getClientThread().sendMessage("Inavalid format! Your answer needed to be a one of: 1, 2, 3, 4");
            }

            currentPlayersAnswered.put(player, true);

            answersReceivedThisRound++;

            if (answersReceivedThisRound >= nrMaxPlayers) {
                notifyAll();
            }

        } else {
            player.getClientThread().sendMessage("You already submited your answer!");
        }

    }

    public void broadcast(String message){
        for(Player player : players){
            player.getClientThread().sendMessage(message);
        }
    }
    @Override
    public void run(){
        broadcast("The game has started!");

        int indexQuestion=0;
        for(Question question:questions){
            indexQuestion++;
            this.currentQuestion=question;
            this.answersReceivedThisRound=0;
            for(Player player:this.players){
                this.currentPlayersAnswered.put(player,false);
            }
            broadcast("\nQuestion number "+indexQuestion+": \n"+question.getText());
            for(int i=0;i<currentQuestion.getOptions().size();i++){
                broadcast((i+1)+") "+currentQuestion.getOptions().get(i));
            }
            this.broadcast("Your answer needs to be the number of the correct answer (ex: 1, 2,3 or 4): ");

            this.currentQuestionStartTime=System.currentTimeMillis();

            synchronized (this){
                while(answersReceivedThisRound < nrMaxPlayers && System.currentTimeMillis() - currentQuestionStartTime <= 15000){
                    try{
                        wait(1500);
                    } catch (InterruptedException exception){
                        Thread.currentThread().interrupt();
                    }
                }
                broadcast("\nTime expired for this round!");
            }
        }


        broadcast("\nThe game has ended\n");
        List<Player> finalTop = players.stream()
                .sorted(Comparator.comparingInt(Player::getScore).reversed()
                        .thenComparingLong(Player::getTotalResponseTime))
                .toList();

        int index=0;
        for(Player player:finalTop){
            index++;
            player.getClientThread().sendMessage("You are number "+index+" on the final clasament");
            player.getClientThread().sendMessage("KILL");
        }
        inProgress=false;
        this.players.clear();
    }
}
