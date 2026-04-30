package compulsory.threads;

import compulsory.canvas.MazePane;
import compulsory.maze.Coordonates;
import compulsory.panels.BunniesPanel;
import compulsory.panels.BunnyPanel;
import compulsory.panels.RobotPanel;
import compulsory.panels.RobotsPanel;
import javafx.application.Platform;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class RobotTask implements Runnable{

    private Integer robotNumber;
    private ExecutingController executingController;
    private MazePane mazePane;
    private RobotPanel robotPanel;
    private BunniesPanel bunniesPanel;
    private RobotsPanel robotsPanel;

    @Override
    public void run(){
        while(executingController.isAlive()){
            synchronized (executingController.getPauseLock()){
                while(!this.robotPanel.isRunning() || this.robotPanel.isParentDisabled()){
                    try{
                        executingController.getPauseLock().wait();
                    } catch (InterruptedException exception){
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                this.taskLogic();
                Platform.runLater(()->{this.mazePane.reDraw();});
            }

            int delayMs=6000-(1000*robotPanel.getSpeed());
            try{
                Thread.sleep(delayMs);
            } catch (InterruptedException exception){
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    private void taskLogic(){
        Coordonates currentCoordonates = this.mazePane.getMaze().getRobots()[this.robotNumber];

        int[] dirRow={1,0,-1,0};
        int[] dirCol={0,-1,0,1};
        int[] nextRow={2,0,-2,0};
        int[] nextCol={0,-2,0,2};
        double[] scores={-100,-100,-100,-100};

        int curRow=currentCoordonates.getRow();
        int curCol=currentCoordonates.getCol();
        var closedDoors=this.mazePane.getMaze().getClosedDoors();
        double maxScore=-100;
        for(int i=0;i<4;i++){
            if(!closedDoors[curRow+dirRow[i]][curCol+dirCol[i]]){
                Coordonates nextLocation=new Coordonates(curRow+nextRow[i],curCol+nextCol[i]);
                int nrRobots=this.mazePane.getMaze().getRobots().length;
                if(this.mazePane.getMaze().robotAlreadyThere(nextLocation,nrRobots) || this.mazePane.getMaze().getFinish().equals(nextLocation)){
                    if(!closedDoors[nextLocation.getRow()+dirRow[i]][nextLocation.getCol()+dirCol[i]]){
                        scores[i]=this.hallScanning(nextLocation,nextLocation,dirRow[i],dirCol[i],nextRow[i],nextCol[i],0);
                    }
                } else {
                    scores[i]=this.hallScanning(currentCoordonates,nextLocation,dirRow[i],dirCol[i],nextRow[i],nextCol[i],0);
                }
            }
            maxScore=maxScore < scores[i]? scores[i]:maxScore;
        }

        if(maxScore!=-100){
            List<Coordonates> possibleRoutes=new ArrayList<>();
            for(int i=0;i<4;i++) if(scores[i]==maxScore){
                Coordonates nextLocation=new Coordonates(curRow+nextRow[i],curCol+nextCol[i]);
                int nrRobots=this.mazePane.getMaze().getRobots().length;
                if(this.mazePane.getMaze().robotAlreadyThere(nextLocation,nrRobots)){
                    nextLocation=new Coordonates(nextLocation.getRow()+nextRow[i],nextLocation.getCol()+nextCol[i]);
                }
                possibleRoutes.add(nextLocation);
            }

            Random random = new Random();
            int whereWeGo=random.nextInt(possibleRoutes.size());
            Coordonates nextLocation=possibleRoutes.get(whereWeGo);

            this.makeStep(nextLocation);
        }
    }

    private double hallScanning(Coordonates startVision,Coordonates currentPosition,int dirRow,int dirCol,int nextRow,int nextCol,double score){
        int currRow=currentPosition.getRow();
        int currCol=currentPosition.getCol();

        var closedDoors=this.mazePane.getMaze().getClosedDoors();
        int nrAliveBunnies=this.mazePane.getMaze().bunnies.size()+1;
        if(this.mazePane.getMaze().bunnyAlreadyThere(currentPosition,nrAliveBunnies)){
            score+=20;
        } else if(this.mazePane.getMaze().getRobotsMemory().contains(currentPosition)){
            score-=0.2;
        }
        if(closedDoors[currRow+dirRow][currCol+dirCol]==true){
            return score;
        }
        return hallScanning(startVision,new Coordonates(currRow+nextRow,currCol+nextCol),dirRow,dirCol,nextRow,nextCol,score);
    }

    private void makeStep(Coordonates whereTo){
        //System.out.println(this.bunnyName + " "+whereTo);
        int nrAliveBunnies=this.mazePane.getMaze().bunnies.size()+1;
        String bunnyName=this.mazePane.getMaze().thereIsBunny(whereTo.getRow(),whereTo.getCol());
        if(!bunnyName.isEmpty()){
            BunnyPanel bunnyPanel = this.bunniesPanel.getBunnyPanel(bunnyName);
            bunnyPanel.setRunning(false);
            bunnyPanel.markVerdict(false);
            this.mazePane.getMaze().eliminateBunny(bunnyName);
            Platform.runLater(()->{this.robotsPanel.incrementScore();});
            Platform.runLater(()->{bunnyPanel.reDraw();});
        }

        this.mazePane.getMaze().moveRobot(this.robotNumber,whereTo);
    }
}
