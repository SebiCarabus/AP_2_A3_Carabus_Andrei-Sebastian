public class Road {
    private String name;
    private int speedLimit;
    private RoadType type;
    private Location location1;
    private Location location2;
    private int length;

    /** Roads are represented by:
     * name,
     * speed limit,
     * type (Ex: Country, Express, etc.),
     * the two locations it unites
     * the length which is calculated with the help of the coordonates of the two locations it unites
     */
    public Road(String name, int speedLimit, RoadType type,Location location1,Location location2) {
        this.name = name;
        this.speedLimit = speedLimit;
        this.type = type;
        this.location1=location1;
        this.location2=location2;
        this.length=(int)Math.sqrt(Math.pow(location2.getX()-location1.getX(),2)+Math.pow(location2.getY()-location1.getY(),2));
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public String getName() {
        return name;
    }

    public RoadType getType() {
        return type;
    }

    public Location getLocation1() {
        return location1;
    }

    public Location getLocation2() {
        return location2;
    }

    public int getLength() {
        return length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public void setType(RoadType type) {
        this.type = type;
    }

    public void setLocation1(Location location1) {
        this.location1 = location1;
        this.length=(int)Math.sqrt(Math.pow(location2.getX()-location1.getX(),2)+Math.pow(location2.getY()-location1.getY(),2));
    }

    public void setLocation2(Location location2) {
        this.location2 = location2;
        this.length=(int)Math.sqrt(Math.pow(location2.getX()-location1.getX(),2)+Math.pow(location2.getY()-location1.getY(),2));
    }

    @Override
    public String toString() {
        return "The road \"" + this.name +"\" with the speed limit of "+this.speedLimit+" has the length of "+this.length+" km" ;
    }

    /**
     * We consider that two roads are equal if they have the same name, same type, same speed limit and unite the same two locations
     */
    @Override
    public boolean equals(Object obj){
        if(obj==null || !(obj instanceof Road)){
            return false;
        }
        Road other=(Road) obj;
        if(this.name.equals(other.name) && this.type==other.type && this.speedLimit==other.speedLimit){
            if (this.location1.equals(other.location1) && this.location2.equals(other.location2)){
                return true;
            }
            return false;
        }
        return false;
    }
}
