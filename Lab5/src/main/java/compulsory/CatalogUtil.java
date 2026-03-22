package compulsory;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class CatalogUtil {
    public static void save(Catalog catalog,String path) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(
                new File(path),
                catalog
        );
    }

    public static Catalog load(String path) throws IOException{
        ObjectMapper objectMapper =  new ObjectMapper();
        Catalog catalog = objectMapper.readValue(
                new File(path),
                Catalog.class
        );
        return catalog;
    }

    public static void view(Item item) throws ItemIsNullException,InvalidLocationException{
        if(item==null){
            throw new ItemIsNullException(new NullPointerException());
        }

        //StringBuilder stringBuilder = new StringBuilder().append("{ \"id\":\""+item.getId()+"\", \"title\":\""+item.getTitle()+"\", \"location\":\""+item.getLocation()+"\"");
        //stringBuilder.append("}");
        //System.out.println(stringBuilder.toString());
        Desktop desktop = Desktop.getDesktop();

        try{
            desktop.browse(new URI(item.getLocation()));
        } catch (Exception exception1){
            try{
                desktop.open(new File(item.getLocation()));
            } catch (Exception exception2) {
                throw new InvalidLocationException();
            }

        }
    }
}
