package advanced;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

@AllArgsConstructor
@Getter
@Setter
public class ReportCommand implements Command {
    private Catalog catalog;
    private String path;
    @Override
    public void execute() throws Exception{
        File htmlFile = new File(this.path);

        try (PrintWriter out = new PrintWriter(new FileWriter(htmlFile))) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head><title>Raport Catalog</title></head>");
            out.println("<body>");
            out.println("<h1>Catalog: " + catalog.getName() + "</h1>");
            out.println("<table border='1' style='width:100%; border-collapse:collapse;'>");
            out.println("<tr><th>ID</th><th>Title</th><th>Location</th></tr>");

            for (Item item : catalog.getItems()) {
                out.println("<tr>");
                out.println("  <td>" + item.getId() + "</td>");
                out.println("  <td>" + item.getTitle() + "</td>");
                out.println("  <td><a href='" + item.getLocation() + "'>" + item.getLocation() + "</a></td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception exception){
            throw exception;
        }

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(htmlFile.toURI());
        }
    }

}
