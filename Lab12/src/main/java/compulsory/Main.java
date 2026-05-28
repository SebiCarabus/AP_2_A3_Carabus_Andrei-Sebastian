package compulsory;

import java.util.Objects;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class Main {
    public static void main(String[] args) {
        String searchedClass="compulsory.DummyClass";
        try{
            Class<?> clazz = Class.forName(searchedClass);
            System.out.println("[INFO]: Clasa "+clazz.getName()+" a fost incarcata cu succes!");

            Object instance = clazz.getDeclaredConstructor().newInstance();

            Method runMethod = clazz.getDeclaredMethod("run");
            System.out.println("[INFO]: Metoda run() a fost gasita");

            System.out.println("[INFO]: Se invoca metoda...\n");
            runMethod.invoke(instance);
        } catch (ClassNotFoundException e) {
            System.err.println("[EROARE]: Clasa nu a fost gasita în classpath: " + e.getMessage());
        } catch (NoSuchMethodException e) {
            System.err.println("[EROARE]: Clasa nu conține o metoda 'run' fara argumente.");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("[EROARE]: Nu s-a putut instantia clasa sau invoca metoda: " + e.getMessage());
        }
    }
}