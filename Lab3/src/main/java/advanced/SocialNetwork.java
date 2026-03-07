package advanced;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocialNetwork {
    private List<Profile> profiles=new ArrayList<>();

    public void addProfile(Profile profile){
        this.profiles.add(profile);
    }

    public void print(){
        List<Profile> sortedList= this.profiles;
        for(int i=0;i<sortedList.size()-1;i++){
            for(int j=i+1;j<sortedList.size();j++){
                if(sortedList.get(i).determineImportance()<sortedList.get(j).determineImportance()){
                    var lowerProfile=sortedList.get(i);
                    var greaterProfile=sortedList.get(j);
                    sortedList.set(i,greaterProfile);
                    sortedList.set(j,lowerProfile);
                }
            }
        }

        sortedList.forEach(profile -> {
            profile.printImportance();
        });
    }

}
