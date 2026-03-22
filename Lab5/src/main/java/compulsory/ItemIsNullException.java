package compulsory;

public class ItemIsNullException extends Exception{
    public ItemIsNullException(Exception exception){
        super("The item is null. ",exception);
    }
}
