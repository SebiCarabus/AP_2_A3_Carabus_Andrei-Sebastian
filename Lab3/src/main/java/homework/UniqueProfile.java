package homework;

public abstract class UniqueProfile implements Profile {
    private static int nextIdNumber=0;
    protected String name;
    protected int id;

    protected UniqueProfile(){
        nextIdNumber++;
        this.id=nextIdNumber;
    }

    protected UniqueProfile(String name){
        nextIdNumber++;
        this.id=nextIdNumber;
        this.name=name;
    }

    protected void setName(String name){
        this.name=name;
    }
}
