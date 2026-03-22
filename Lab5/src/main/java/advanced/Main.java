package advanced;

import java.util.*;
import java.util.stream.Collectors;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Main app = new Main();
        //app.testCreateSave();
        //app.testLoadView();
        //app.testLoadList();
        //app.testLoadReport();
        app.testLargeCover();
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

    private void testLargeCover() {
        Catalog bigCatalog = new Catalog("BigData");
        int numItems = 1000;
        int numConcepts = 500;
        List<String> allPossibleConcepts = new ArrayList<>();
        for(int i=0; i<numConcepts; i++) {
            allPossibleConcepts.add("C" + i);
        }

        Random rand = new Random();
        for(int i=0; i<numItems; i++) {
            Item item = new Item("ID"+i, "Title"+i, "Loc"+i);
            for(int j=0; j<5; j++) {
                item.addConcept(allPossibleConcepts.get(rand.nextInt(numConcepts)));
            }
            bigCatalog.getItems().add(item);
        }

        List<String> targetConcepts = new ArrayList<>(allPossibleConcepts.subList(0, 100));

        long startTime = System.currentTimeMillis();
        CoverCommand coverCommand = new CoverCommand(bigCatalog, targetConcepts);
        try {
            coverCommand.execute();
        } catch (Exception exception){
            System.out.println("Error: "+exception);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("\nExecution time for " + numItems + " items: " + (endTime - startTime) + "ms");
    }
}
