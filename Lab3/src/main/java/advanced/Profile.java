package advanced;
import java.util.Map;
public interface Profile extends Comparable<Profile>{
    public int getId();
    public String getName();
    public int determineImportance();
    public void printImportance();
    public Map<Profile,String> getRelationShips();
    @Override
    default int compareTo(Profile other){
        return this.getName().compareTo(other.getName());
    }
}
