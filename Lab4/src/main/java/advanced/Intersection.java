package advanced;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Intersection implements Comparable<Intersection>{
    private String name;

    @Override
    public int compareTo(Intersection o) {
        return this.name.compareTo(o.name);
    }
}
