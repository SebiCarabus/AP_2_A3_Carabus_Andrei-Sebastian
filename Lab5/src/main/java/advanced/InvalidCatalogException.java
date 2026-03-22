package advanced;

public class InvalidCatalogException extends Exception{
    public InvalidCatalogException(Exception exception){
        super("Invalid catalog file.", exception);
    }
}
