package advanced.DTO;

import advanced.domain.Actor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ActorDTO {
    //private int id;
    private String name;

    public ActorDTO (Actor actor){
        if(actor!=null){
            //this.id = actor.getId();
            this.name = actor.getName();
        }
    }
}
