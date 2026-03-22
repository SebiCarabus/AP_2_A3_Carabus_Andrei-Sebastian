package homework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AddCommand implements Command{
    private Catalog catalog;
    private Item item;

    @Override
    public void execute()throws Exception{
        this.catalog.getItems().add(this.item);
    }
}
