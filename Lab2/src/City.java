public final class City extends Location{
    public City(){super();}
    public City(String name){
        super(name);
    }
    @Override
    public String getName(){
        return "City "+super.name;
    }
    @Override
    public String toString(){
        return "The city "+super.toString();
    }
}