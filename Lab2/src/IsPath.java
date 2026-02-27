public class IsPath extends Algorithm{
    private Location startLocation;
    private Location endLocation;

    /**
     * IsPath verifies if a given graph there is at least one path between two locations and if it is it gives one random one
     */
    IsPath(Problem problem,Location start, Location end){
        super(problem);
        this.startLocation=start;
        this.endLocation=end;
    }

    /**
     * The implementation of this problem is resolved by a DFS where we remember the path we take and which stops if we encounter the desired destionation
     */
    private boolean dfs(Location currentLocation, Location destinationLocation, Location[] viz,Solution sol){
        markVisitedLocation(viz,currentLocation);
        sol.pushLocation(currentLocation);
        if(currentLocation.equals(destinationLocation)){
            return true;
        }
        for(int i=0;i<this.problem.getNumRoads();i++){
            Location nextLocation=null;
            if(this.problem.getRoads()[i].getLocation1().equals(currentLocation)){
                nextLocation=this.problem.getRoads()[i].getLocation2();
            } else if (this.problem.getRoads()[i].getLocation2().equals(currentLocation)){
                nextLocation=this.problem.getRoads()[i].getLocation1();
            }
            if(nextLocation!=null && !isVisited(viz,nextLocation)){
                if(dfs(nextLocation,destinationLocation,viz,sol)){
                    return true;
                }
            }
        }
        sol.popLocation();
        return false;
    }


    /**
     * Here we configure the solution of the problem we run the algorithm on
     */
    @Override
    public Solution solve(){
        if(this.problem.isValid() && super.checkLocations(this.startLocation,this.endLocation)){
            Location[] viz=new Location[this.problem.getNumLocations()];
            Solution sol=new Solution(this.problem.getNumLocations());
            this.dfs(this.startLocation,this.endLocation,viz,sol);
            if(sol.getSolLength()==0){
                sol.setDescription("There is no path from \""+this.startLocation.getName()+"\" to \""+this.endLocation.getName()+"\"");
            } else {
                sol.setDescription("A path from \""+this.startLocation.getName()+"\" to \""+this.endLocation.getName()+"\" was found");
            }
            return sol;
        }
        return new Solution(0);
    }
}
