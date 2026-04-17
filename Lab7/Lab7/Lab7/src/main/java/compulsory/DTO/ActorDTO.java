package compulsory.DTO;

import compulsory.domain.Actor;
import compulsory.domain.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ActorDTO {
    //private int id;
    private String name;

    public ActorDTO (Actor actor){
        //this.id = actor.getId();
        this.name = actor.getName();
    }
}
