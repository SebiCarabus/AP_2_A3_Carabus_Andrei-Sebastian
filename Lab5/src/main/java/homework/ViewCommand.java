package homework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.File;
import java.net.URI;

@AllArgsConstructor
@Getter
@Setter
public class ViewCommand implements Command{
    private Item item;
    @Override
    public void execute()throws Exception{
        if(this.item==null){
            throw new ItemIsNullException(new NullPointerException());
        }

        Desktop desktop = Desktop.getDesktop();

        try{
            desktop.browse(new URI(this.item.getLocation()));
        } catch (Exception exception1){
            try{
                desktop.open(new File(this.item.getLocation()));
            } catch (Exception exception2) {
                throw new InvalidLocationException();
            }

        }
    }
}
