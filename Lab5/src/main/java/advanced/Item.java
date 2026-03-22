package advanced;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Item implements Serializable {
    private String id;
    private String title;
    private String location;

    private List<String> concepts = new ArrayList<>();

    public Item(String id, String title, String location) {
        this.id = id;
        this.title = title;
        this.location = location;
    }

    public void addConcept(String concept){
        this.concepts.add(concept);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder().append("{ \"id\":\""+this.id+"\", \"title\":\""+this.title+"\", \"location\":\""+this.title+"\"");
        if(!this.concepts.isEmpty()){
            stringBuilder.append(", \"tags\":[ ");
            int index=0;
            for(var concept:this.concepts){
                if (index>0){
                    stringBuilder.append(", ");
                }
                stringBuilder.append("\""+concept+"\"");
                index++;
            }
            stringBuilder.append(" ]");
        }
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}
