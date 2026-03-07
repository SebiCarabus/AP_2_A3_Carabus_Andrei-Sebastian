package advanced;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Programmer extends Person {
    private ProgramingLanguage language;
    public Programmer(String name, ProgramingLanguage language){
        super(name);
        this.language=language;
    }
}
