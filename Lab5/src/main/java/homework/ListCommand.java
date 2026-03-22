package homework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ListCommand implements Command{
    private Catalog catalog;
    @Override
    public void execute()throws Exception{
        this.catalog.getItems().forEach(item -> System.out.println(item.toString()));
    }
}
