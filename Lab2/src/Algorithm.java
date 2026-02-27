

public abstract class Algorithm {
    protected Problem problem;

    /**
     * An algorithm needs a problem to solve it
     */
    protected Algorithm(Problem problem){this.problem=problem;}

    /**
     * Down are some utilty functions which many graph algorithms might need
     */
    protected static void markVisitedLocation(Location[] viz, Location location){
        for(int i=0;i< viz.length;i++){
            if(viz[i]==null){
                viz[i]=location;
                return;
            }
        }
    }
    protected static boolean isVisited(Location[] viz, Location location){
        for(int i=0;i<viz.length;i++){
            if(location.equals(viz[i])){
                return true;
            }
        }
        return false;
    }

    protected Road directRoad(Location location1,Location location2){
        for(int i=0;i<this.problem.getNumRoads();i++){
            Road curRoad=this.problem.getRoads()[i];
            if(curRoad.getLocation1().equals(location1) && curRoad.getLocation2().equals(location2)){
                return curRoad;
            } else if (curRoad.getLocation1().equals(location2) && curRoad.getLocation2().equals(location1)){
                return curRoad;
            }
        }
        return null;
    }

    protected boolean checkLocations(Location location1, Location location2){
        boolean found1=false;
        boolean found2=false;
        for(int i=0;i<this.problem.getNumLocations();i++){
            if(this.problem.getLocations()[i].equals(location1)){
                found1=true;
            }
            if(this.problem.getLocations()[i].equals(location2)){
                found2=true;
            }
            if(found1 && found2){
                return true;
            }
        }
        return false;
    }

    /**
     * Each algorithm needs to provide a solution
     */
    public abstract Solution solve();
}
