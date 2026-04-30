package compulsory.threads;

import compulsory.canvas.MazePane;
import compulsory.panels.BunniesPanel;
import compulsory.panels.CountDown;
import compulsory.panels.RobotPanel;
import compulsory.panels.RobotsPanel;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CowntDownTask implements Runnable{

    private ExecutingController executingController;
    private MazePane mazePane;
    private BunniesPanel bunniesPanel;
    private RobotsPanel robotsPanel;
    private CountDown countDown;

    @Override
    public void run(){
        Thread.currentThread().setPriority(10);
        while(executingController.isAlive()){
            synchronized (executingController.getPauseLock()){
                while(!this.countDown.isCounting()){
                    try{
                        executingController.getPauseLock().wait();
                    } catch (InterruptedException exception){
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                this.taskLogic();
                Platform.runLater(()->{this.countDown.reDraw();});
            }
            int delayMs=1000;
            try{
                Thread.sleep(delayMs);
            } catch (InterruptedException exception){
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void taskLogic(){
        Integer nrSeconds = this.countDown.getNumberSeconds() - 1;
        this.countDown.setNumberSeconds(nrSeconds);
        if(nrSeconds<=0){
            this.countDown.setCounting(false);
            this.finalMessage(true);
        } else if (this.mazePane.getMaze().getBunnies().isEmpty()){
            this.countDown.setCounting(false);
            this.finalMessage(false);
        }
    }

    private void finalMessage(boolean endedTime){
        this.executingController.killThreadsExcluding(Thread.currentThread());
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game results");
            if(endedTime){
                alert.setTitle("Time's Up");
            }
            String message = "It is a DRAW!";
            String secondMessage = "It has been a tight match!";
            if(this.bunniesPanel.getScore() > this.robotsPanel.getScore()){
                message = "Bunnies WON!";
                secondMessage ="Robots had lost...";
            } else if (this.bunniesPanel.getScore() < this.robotsPanel.getScore()){
                message = "Robots WON!";
                secondMessage ="Bunnies had lost...";
            }

            alert.setContentText(secondMessage);
            alert.setHeaderText(message);
            alert.showAndWait();
            System.exit(0);
        });
    }
}
