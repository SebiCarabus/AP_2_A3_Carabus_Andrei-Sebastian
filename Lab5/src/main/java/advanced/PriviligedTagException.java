package advanced;

public class PriviligedTagException extends Exception{
    public PriviligedTagException(String tagName){
        super("You cannot add "+tagName+" as a tag.");
    }
}
