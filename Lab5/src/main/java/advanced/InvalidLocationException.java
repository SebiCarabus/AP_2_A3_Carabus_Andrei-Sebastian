package advanced;

public class InvalidLocationException extends Exception{
    public InvalidLocationException (){
        super("The item location coudn't be accesed.");
    }
}
