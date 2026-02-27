public final class GasStation extends Location{
    public GasStation(){super();}
    public GasStation(String name){
        super(name);
    }
    @Override
    public String getName(){
        return "Gas Station "+super.name;
    }
    @Override
    public String toString(){
        return "The gas station "+super.toString();
    }
}