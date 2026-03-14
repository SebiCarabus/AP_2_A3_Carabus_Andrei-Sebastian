package compulsory;

import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Intersection implements Comparable<Intersection>{
    private String name;

    @Override
    public int compareTo(Intersection o) {
        return this.name.compareTo(o.name);
    }
}
