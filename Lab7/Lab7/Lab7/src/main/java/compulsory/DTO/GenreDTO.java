package compulsory.DTO;

import compulsory.domain.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GenreDTO {
    //private int id;
    private String name;

    public GenreDTO (Genre genre){
        //this.id = genre.getId();
        this.name = genre.getName();
    }
}
