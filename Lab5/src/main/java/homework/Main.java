package homework;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Main app = new Main();
        app.testCreateSave();
        app.testLoadView();
        app.testLoadList();
        app.testLoadReport();
    }
    private void testCreateSave(){
        Catalog catalog = new Catalog("MyRefs");
        var specificationBook1 = new Item("java25","The Java Language Specification","https://docs.oracle.com/javase/specs/jls/se25/jls25.pdf");
        var specificationBook2 = new Item("jvm25","The Java Virtual Machine Specification","https://docs.oracle.com/javase/specs/jvms/se25/html/index.html");
        var addCommand= new AddCommand(catalog,specificationBook1);
        try{
            addCommand.execute();
            addCommand.setItem(specificationBook2);
            try{
                addCommand.execute();
            } catch (Exception exception){
                System.out.println(exception);
            }
        } catch (Exception exception){
            System.out.println(exception);
        }

        var saveCommand=new SaveCommand(catalog,"d:/archive/catalog.json");
        try{
            saveCommand.execute();
        } catch (Exception exception){
            System.out.println(exception);
        }
    }

    private void testLoadView(){
        try{
            var loadCommand = new LoadCommand("d:/archive/catalog.json");
            loadCommand.execute();
            Catalog catalog = loadCommand.getCatalog();
            try{
                var viewCommand=new ViewCommand(catalog.findById("java25"));
                viewCommand.execute();
            } catch (Exception exception){
                System.out.println(exception);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void testLoadList(){
        try{
            var loadCommand = new LoadCommand("d:/archive/catalog.json");
            loadCommand.execute();
            Catalog catalog = loadCommand.getCatalog();
            try{
                var listCommand=new ListCommand(catalog);
                listCommand.execute();
            } catch (Exception exception){
                System.out.println(exception);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void testLoadReport(){
        try{
            var loadCommand = new LoadCommand("d:/archive/catalog.json");
            loadCommand.execute();
            Catalog catalog = loadCommand.getCatalog();
            try{
                var reportCommand=new ReportCommand(catalog,"d:/archive/report_java_app.html");
                reportCommand.execute();
            } catch (Exception exception){
                System.out.println(exception);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
