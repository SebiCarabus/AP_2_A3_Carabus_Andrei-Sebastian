public class Dijkstra extends Algorithm{
    private Location startLocation;
    private Location endLocation;
    private static final int INF= 1_000_000_000;

    /**
     * This algorithm needs the problem(the graph) and the two locations for which we want to determine a path of minimum length
     */
    Dijkstra(Problem problem,Location start, Location end){
        super(problem);
        this.startLocation=start;
        this.endLocation=end;
    }

    class LocationMap{
        private Location key;
        private Location value;

        LocationMap(){this.value=null;}

        public void setKey(Location key) {
            this.key = key;
        }

        public void setValue(Location value) {
            this.value = value;
        }

        public Location getValue() {
            return value;
        }

        public Location getKey() {
            return key;
        }
    }

    /**
     * The solution of this problem is implemented by a Dijsktra because we want to find out the path of minimum length between two locations
     * And also we configure the solution of the problem we run the algorithm on
     */
    @Override
    public Solution solve(){
        int n=super.problem.getNumLocations();
        Solution sol=new Solution(n);
        sol.setDescription("There is no path of minimal length from \""+this.startLocation.getName()+"\" to \""+this.endLocation.getName()+"\"");
        if(!this.problem.isValid() || !super.checkLocations(this.startLocation,this.endLocation)){
            return sol;
        }


        Location[] viz=new Location[n];
        LocationMap[] before=new LocationMap[n];
        int[] minDistance=new int[n];
        int indexDestination=0;
        for(int i=0;i<n;i++){
            Location l=super.problem.getLocations()[i];
            before[i]=new LocationMap();
            before[i].setKey(l);
            if(!this.startLocation.equals(l)){
                before[i].setValue(this.startLocation);
                minDistance[i]=INF;
                Road directRoad=super.directRoad(l,this.startLocation);
                if(directRoad!=null){
                    minDistance[i]=directRoad.getLength();
                }
                if(this.endLocation.equals(l)){
                    indexDestination=i;
                }
            } else {
                minDistance[i]=0;
            }
        }

        markVisitedLocation(viz,this.startLocation);
        int nrMarkedLocations=1;

        while(nrMarkedLocations!=n){
            int minDistanceNotVizited=INF;
            Location nextLocationToMark=null;
            for(int i=0;i<n;i++){
                Location l=super.problem.getLocations()[i];
                if(!isVisited(viz,l)){
                    if(minDistance[i]<=minDistanceNotVizited){
                        nextLocationToMark=l;
                        minDistanceNotVizited=minDistance[i];
                    }
                }
            }
            markVisitedLocation(viz,nextLocationToMark);
            nrMarkedLocations++;
            for(int j=0;j<n;j++){
                Location l=super.problem.getLocations()[j];
                Road directRoad=super.directRoad(l,nextLocationToMark);
                if(directRoad!=null && !isVisited(viz,l) && minDistance[j]>minDistanceNotVizited+directRoad.getLength()){
                    minDistance[j]=minDistanceNotVizited+directRoad.getLength();
                    before[j].setValue(nextLocationToMark);
                }
            }
        }

        if(minDistance[indexDestination]==INF){
            return sol;
        }
        Location[] reverseSolOrder=new Location[n];
        int i=indexDestination;
        int solLength=0;
        while(before[i].getValue()!=null){
            reverseSolOrder[solLength]=before[i].getKey();
            solLength++;
            for(int j=0;j<n;j++){
                if(before[j].getKey().equals(before[i].getValue())){
                    i=j;
                    break;
                }
            }
        }
        reverseSolOrder[solLength]=this.startLocation;
        solLength++;
        for(int j=solLength-1;j>=0;j--){
            sol.pushLocation(reverseSolOrder[j]);
        }
        sol.setDescription("A path of minimal length from \""+this.startLocation.getName()+"\" to \""+this.endLocation.getName()+"\" was found");
        return sol;
    }
}
