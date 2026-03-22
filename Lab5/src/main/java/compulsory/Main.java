package compulsory;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Main app = new Main();
        app.testCreateSave();
        app.testLoadView();
    }
    private void testCreateSave(){
        Catalog catalog = new Catalog("MyRefs");
        var specificationBook1 = new Item("java25","The Java Language Specification","https://docs.oracle.com/javase/specs/jls/se25/jls25.pdf");
        var specificationBook2 = new Item("jvm25","The Java Virtual Machine Specification","https://docs.oracle.com/javase/specs/jvms/se25/html/index.html");
        catalog.add(specificationBook1);
        catalog.add(specificationBook2);

        try {
            CatalogUtil.save(catalog,"d:/archive/catalog.json");
        } catch (IOException exception){
            System.out.println(exception);
        }
    }

    private void testLoadView(){
        try{
            Catalog catalog = CatalogUtil.load("d:/archive/catalog.json");
            CatalogUtil.view(catalog.findById("java25"));
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
