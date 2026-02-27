public class Problem {
    private Location[] locations;
    private Road[] roads;
    private int numLocations;
    private int numRoads;

    public Problem(Location[] locations, Road[] roads){
        this.numLocations=locations.length;
        this.numRoads=roads.length;
        this.locations=locations;
        this.roads=roads;
    }

    public boolean isValid(){
        for(int i=0;i<this.numLocations-1;i++){
            for(int j=i+1;j<this.numLocations;j++){
                if(this.locations[i].equals(this.locations[j])){
                    return false;
                }
            }
        }
        for(int i=0;i<this.numRoads-1;i++){
            if(this.roads[i].getLocation1().equals(this.roads[i].getLocation2())){
                return false;
            }
            for(int j=i+1;j<this.numRoads;j++){
                if(this.roads[i].equals(this.roads[j])){
                    return false;
                }
            }
        }

        return true;
    }

    public int getNumLocations() {
        return numLocations;
    }

    public Location[] getLocations() {
        return locations;
    }

    public Road[] getRoads() {
        return roads;
    }

    public int getNumRoads() {
        return numRoads;
    }
}
