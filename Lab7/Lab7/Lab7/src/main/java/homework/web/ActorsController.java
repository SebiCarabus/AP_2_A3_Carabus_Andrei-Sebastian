package homework.web;

import homework.DTO.ActorDTO;
import homework.domain.Actor;
import homework.persistence.dao.ActorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/actors")
public class ActorsController {
    @Autowired
    private ActorDAO actorDAO;

    @GetMapping
    public List<ActorDTO> getAll(){
        List<Actor> actors = actorDAO.findAll();
        List<ActorDTO> actorsDTO = new ArrayList<ActorDTO>();
        actors.forEach(actor -> {
            actorsDTO.add(new ActorDTO((actor)));
        });
        return actorsDTO;
    }
}
