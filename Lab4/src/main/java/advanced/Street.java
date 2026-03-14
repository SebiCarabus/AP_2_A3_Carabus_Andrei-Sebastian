package advanced;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class Street implements Comparable<Street> {
    private String name;
    private int length;
    private Intersection intersection1;
    private Intersection intersection2;

    @Override
    public int compareTo(Street o) {
        return this.length-o.length;
    }
}
