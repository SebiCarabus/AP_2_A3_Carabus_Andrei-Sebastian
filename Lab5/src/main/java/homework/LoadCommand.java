package homework;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;


@Getter
public class LoadCommand implements Command{
    private String path;
    private Catalog catalog;

    public LoadCommand(String path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void execute() throws Exception{
        ObjectMapper objectMapper =  new ObjectMapper();
        Catalog catalog = objectMapper.readValue(
                new File(path),
                Catalog.class
        );
        this.catalog=catalog;
    }
}
