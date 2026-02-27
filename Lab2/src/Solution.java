/**
 * We consider that a Solution is made of the order of locations a path travels
 * For further details we provide a description field on which we say which algorithm generated this solution
 */

public class Solution {
    private Location[] locations;
    private int solLength;
    private String description;
    public Solution(int length){this.locations=new Location[length];}
    public void pushLocation(Location newLocation){
        this.locations[this.solLength]=newLocation;
        this.solLength++;
    }
    public void popLocation(){
        this.solLength--;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public int getSolLength(){
        return solLength;
    }

    /**
     * If the solustions has a path we show it which means the problem was solved
     * If not we return just the descriptions
     */
    @Override
    public String toString(){
        if(this.solLength==0){
            return this.description;
        } else {
            StringBuilder sb=new StringBuilder(this.description+'\n');
            for(int i=0;i<this.solLength;i++){
                if(i!=0){
                    sb.append(" -> ");
                }
                sb.append(this.locations[i].getName());
            }
            return sb.toString();
        }
    }
}
