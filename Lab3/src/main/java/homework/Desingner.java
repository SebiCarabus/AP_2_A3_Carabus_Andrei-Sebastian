package homework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Desingner extends Person {
    private Color preferedColor;
    Desingner(String name,Color color){
        super(name);
        this.preferedColor=color;
    }
}
