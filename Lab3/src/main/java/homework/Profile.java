package homework;

public interface Profile extends Comparable<Profile>{
    public int getId();
    public String getName();
    public int determineImportance();
    public void printImportance();
    @Override
    default int compareTo(Profile other){
        String myName = this.getName();
        String otherName = other.getName();
        if(myName!=null){
            return myName.compareTo(otherName);
        }
        return -1;
    }
}
