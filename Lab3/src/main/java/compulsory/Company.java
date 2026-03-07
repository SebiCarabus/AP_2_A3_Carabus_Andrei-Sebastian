package compulsory;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Company extends UniqueProfile implements Comparable<Profile>{

    public Company(){
        super();
    }

    public Company(String name){
        super(name);
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
