package compulsory;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Catalog implements Serializable {
    private String name;
    private List<Item> items = new ArrayList<>();

    public Catalog(String name){
        this.name=name;
    }

    public void add(Item item){
        this.items.add(item);
    }

    public Item findById(String id){
        return this.items.stream()
                .filter(item -> Objects.equals(item.getId(),id))
                .findFirst().orElse(null);
    }
}
