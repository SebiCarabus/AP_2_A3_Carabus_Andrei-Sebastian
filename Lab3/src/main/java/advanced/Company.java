package advanced;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Company extends UniqueProfile implements Comparable<Profile>{

    private Map<Profile,String> relationShips = new HashMap<>();
    private String industry;

    public Company(){
        super();
    }

    public Company(String name){
        super(name);
    }

    public void addRelationShip(Profile profile, String name){
        if(profile!=null && name!=null){
            this.relationShips.put(profile,name);
        }
    }

    public int determineImportance(){
        return this.relationShips.size();
    }

    @Override
    public void printImportance(){
        System.out.print(this.name+" has an importance of "+this.determineImportance());
        if(this.determineImportance()==0){
            System.out.println();
        } else {
            System.out.println(" and has relationships with:");
            for(Profile profile:this.relationShips.keySet()){
                if(profile!=null && profile.getName()!=null) {
                    System.out.println("\t* " + profile.getName());
                }
            }
        }
    }

    @Override
    public int getId(){
        return this.id;
    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public int compareTo(Profile other){
        if(this.name!=null){
            return this.name.compareTo(other.getName());
        }
        return -1;
    }
}
