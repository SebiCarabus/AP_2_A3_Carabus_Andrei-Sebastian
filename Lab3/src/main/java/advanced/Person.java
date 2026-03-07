package advanced;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Person extends UniqueProfile implements Comparable<Profile>{
    private Map<Profile,String> relationShips = new HashMap<>();
    private LocalDate birthDate;

    public Person(){
        super();
    }

    public Person(String name){
        super(name);
    }

    public void addRelationShip(Profile profile, String name){
        if(profile!=null && name!=null){
            this.relationShips.put(profile,name);
        }
    }

    @Override
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


