public final class AirPort extends Location{
    public AirPort(){super();}
    public AirPort(String name){
        super(name);
    }
    @Override
    public String getName(){
        return "Air Port "+super.name;
    }
    @Override
    public String toString(){
        return "The air port "+super.toString();
    }
}
