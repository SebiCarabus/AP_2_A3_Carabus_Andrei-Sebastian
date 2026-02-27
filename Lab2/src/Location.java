/**
 * This abstract class is used as the base structure for locations like Air Ports, Cities and Gas Stations.
 */
public abstract sealed class Location permits AirPort,GasStation,City{
    /**
     * A location is represented by its name and xOy coordonates.
     */
    protected String name;
    protected int x;
    protected int y;

    public Location(){}

    public Location(String name){
        this.name=name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public abstract String getName();

    @Override
    public String toString() {
        return "\""+this.name+"\" located at the coordonates: X="+this.x+" Y="+this.y;
    }

    /**
     * We identify that two locations are equal if their specific class is a like and if they have the same name and coordonates.
     * For Example a city named Suceava is not the same as an airport name Suceava even if they have the same coordonates.
     */
    @Override
    public boolean equals(Object obj){
        if(obj == null || this.getClass()!=obj.getClass()){
            return false;
        }
        Location other = (Location) obj;
        if(this.name.equals(other.name) && this.x==other.x && this.y==other.y){
            return true;
        }
        return false;
    }
}
