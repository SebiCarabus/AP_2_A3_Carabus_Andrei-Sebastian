package compulsory.maze;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Maze {
    private int size;
    private int sizeMatrix;
    public Coordonates finish;
    public Map<String, Coordonates> bunnies=new LinkedHashMap<>();
    public Coordonates[] robots;
    public boolean[][] closedDoors;

    private static final int BUNNY_MEMORY=5;
    private static final int ROBOTS_MEMORY=15;
    public Queue<Coordonates> robotsMemory=new LinkedList<>();
    public Map<String,Queue<Coordonates>> bunniesMemory=new HashMap<>();

    public Maze(int size,int numberBunnies,int numberRobots){
        this.size=size;
        this.sizeMatrix=2*this.size+1;
        this.closedDoors=new boolean[this.sizeMatrix][this.sizeMatrix];
        int maxTryNumber=100;
        int generatingAttempts=0;
        do {
            this.finish = generateRandomCellCoordinates();
            Integer currentLength = 0;
            for (int i=0;i<numberBunnies;i++) {
                String name;
                do {
                    currentLength++;
                    name="B"+currentLength.toString();
                    this.bunniesMemory.put(name,new LinkedList<>());
                    this.bunnies.put(name,generateRandomCellCoordinates());
                    currentLength--;
                } while (this.bunnyAlreadyThere(this.bunnies.get(name),currentLength) || this.bunnies.get(name).equals(this.finish));
                currentLength++;
            }
            this.robots = new Coordonates[numberRobots];
            currentLength = 0;
            for (int i=0;i<numberRobots;i++) {
                do {
                    this.robots[i] = generateRandomCellCoordinates();
                } while (this.bunnyAlreadyThere(this.robots[i],this.bunnies.size()+1) || this.robots[i].equals(this.finish) || this.robotAlreadyThere(this.robots[i], currentLength));
                currentLength++;
            }
            int numberTries=0;
            boolean nextGeneration=false;
            do {
                this.createRandomMaze();
                numberTries++;
                if(numberTries>=maxTryNumber){
                    nextGeneration=true;
                }
            } while (!this.validate() && !nextGeneration);

            if(!nextGeneration) {
                break;
            }
            generatingAttempts++;
            if(generatingAttempts>=maxTryNumber){
                break;
            }
        } while(true);
    }
    public boolean bunnyAlreadyThere(Coordonates location, int currentLength){
        String name;
        for(Integer i=0;i<currentLength;i++){
            name="B"+i.toString();
            if(location.equals(this.bunnies.get(name))){
                return true;
            }
        }
        return false;
    }
    public boolean robotAlreadyThere(Coordonates location, int currentLength){
        for(int i=0;i<currentLength;i++){
            if(location.equals(this.robots[i])){
                return true;
            }
        }
        return false;
    }
    private Coordonates generateRandomCellCoordinates(){
        Random random=new Random();
        int row=random.nextInt(this.size)*2+1;
        int col=random.nextInt(this.size)*2+1;
        return new Coordonates(row,col);
    }
    private void createRandomMaze(){
        Random random=new Random();
        for(int i=0;i<this.sizeMatrix;i++){
            for(int j=0;j<this.sizeMatrix;j++){
                if(i%2==1 && j%2==0 || i%2==0 && j%2==1){
                    if(i==0||j==0||i==this.sizeMatrix-1||j==this.sizeMatrix-1){
                        this.closedDoors[i][j]=true;
                    } else {
                        this.closedDoors[i][j]=random.nextBoolean();
                    }
                } else {
                    this.closedDoors[i][j]=true;
                }
            }
        }
    }
    private boolean existsPathBetween(Coordonates start, Coordonates finsih, boolean [][]visited){
        if(start.equals(finsih)){
            return true;
        }
        int rowStart=start.getRow();
        int colStart=start.getCol();
        visited [rowStart][colStart]=true;
        Coordonates[] nextDoor={
                new Coordonates(rowStart,colStart-1),
                new Coordonates(rowStart+1,colStart),
                new Coordonates(rowStart,colStart+1),
                new Coordonates(rowStart-1,colStart)
        };
        Coordonates[] nextDestination={
                new Coordonates(rowStart,colStart-2),
                new Coordonates(rowStart+2,colStart),
                new Coordonates(rowStart,colStart+2),
                new Coordonates(rowStart-2,colStart)
        };
        boolean[] results={false,false,false,false};
        for(int i=0;i<4;i++){
            if(this.closedDoors[nextDoor[i].getRow()][nextDoor[i].getCol()]==false && visited[nextDestination[i].getRow()][nextDestination[i].getCol()]==false){
                results[i]=existsPathBetween(nextDestination[i],finsih,visited);
            }
        }
        return results[0]||results[1]||results[2]||results[3];
    }
    private boolean validate(){
        boolean [][] visted=new boolean[this.sizeMatrix][this.sizeMatrix];
        Map<String,Boolean> bunniesVisited=new HashMap<>();
        for(var bunny:this.bunnies.entrySet()){
            if(!this.existsPathBetween(bunny.getValue(),this.finish,visted)){
                return false;
            }
            bunniesVisited.put(bunny.getKey(),false);
        }

        for(var bunnyVisited:bunniesVisited.entrySet()){
            if(bunnyVisited.getValue()==true){
                for(int i=0;i<this.robots.length;i++){
                    visted=new boolean[this.sizeMatrix][this.sizeMatrix];
                    if(existsPathBetween(this.robots[i],this.bunnies.get(bunnyVisited.getKey()),visted)){
                        break;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public void drawMaze(){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<this.sizeMatrix;i++){
            for(int j=0;j<this.sizeMatrix;j++){
                if(i%2==0&&j%2==0){
                    stringBuilder.append("+");
                } else if(i%2==1 && j%2==1){
                    String bunnyName=this.thereIsBunny(i,j);
                    int robotNumber=this.thereIsRobot(i,j);
                    if(!bunnyName.isEmpty()){
                        stringBuilder.append(bunnyName);
                    } else if(robotNumber!=0){
                        stringBuilder.append("R"+robotNumber);
                    } else if(i==finish.getRow()&&j==finish.getCol()){
                        stringBuilder.append("✅");
                    } else{
                        stringBuilder.append("  ");
                    }
                } else if (j%2==0){
                    if(this.closedDoors[i][j]==true){
                        stringBuilder.append("|");
                    } else{
                        stringBuilder.append(" ");
                    }
                } else {
                    if(this.closedDoors[i][j]==true){
                        stringBuilder.append("--");
                    } else{
                        stringBuilder.append("  ");
                    }
                }
            }
            stringBuilder.append("\n");
        }
        System.out.println(stringBuilder.toString()+"\n");
    }
    public String thereIsBunny(int row, int col){
        Coordonates location=new Coordonates(row,col);
        for(var bunny:this.bunnies.entrySet()){
            if(location.equals(bunny.getValue())){
                return bunny.getKey();
            }
        }
        return new String();
    }
    public int thereIsRobot(int row, int col){
        Coordonates location=new Coordonates(row,col);
        for(int i=0;i<this.robots.length;i++){
            if(location.equals(this.robots[i])){
                return i+1;
            }
        }
        return 0;
    }
    public Coordonates getBunnyCoordonates(String bunnyName){
        for(var bunny:this.bunnies.entrySet()){
            if(bunnyName.equals(bunny.getKey())){
                return bunny.getValue();
            }
        }
        return null;
    }

    public void eliminateBunny(String bunnyName){
        this.bunnies.remove(bunnyName);
        this.bunniesMemory.remove(bunnyName);
    }

    public void moveBunny(String bunnyName,Coordonates where){
        Queue<Coordonates> memory = this.bunniesMemory.get(bunnyName);
        if (memory.size() >= BUNNY_MEMORY) {
            memory.poll();
        }
        memory.add(where);
        this.bunnies.put(bunnyName, where);
    }

    public void moveRobot(int numRobot,Coordonates where){
        Queue<Coordonates> memory = this.robotsMemory;
        if (memory.size() >= ROBOTS_MEMORY) {
            memory.poll();
        }
        memory.add(where);
        this.robots[numRobot]=where;
    }
}
