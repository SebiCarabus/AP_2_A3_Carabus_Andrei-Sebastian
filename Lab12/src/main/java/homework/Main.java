package homework;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        File folder = new File("C:\\Users\\Sebi\\Desktop\\Java\\Lab12\\src\\main\\java\\homework\\test");

        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Folderul specificat nu exista sau nu este valid!");
            return;
        }
        
        URL[] urls = { folder.toURI().toURL() };
        try (URLClassLoader classLoader = new URLClassLoader(urls)) {

            List<Class<?>> annotationTypes = new ArrayList<>();
            List<Class<?>> publicClasses = new ArrayList<>();
            
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".class"));
            if (files == null || files.length == 0) {
                System.out.println("Nu s-au gasit fisiere .class în folder-ul: "+folder.toURI().toURL());
                return;
            }

           
            for (File file : files) {
                String className = file.getName().replace(".class", "");
                
                Class<?> clazz = classLoader.loadClass(className);

                if (clazz.isAnnotation()) {
                    annotationTypes.add(clazz);
                } else if (Modifier.isPublic(clazz.getModifiers()) && !clazz.isInterface()) {
                    publicClasses.add(clazz);   
                }
            }

            System.out.println(">> Adnotari identificate: " + annotationTypes.size());
            for (Class<?> ann : annotationTypes) {
                System.out.println("   - @" + ann.getSimpleName());
            }

            System.out.println("\n==========================================\n");
            
            for (Class<?> clazz : publicClasses) {
                System.out.println(">> Prototip Clasa Publica: " + clazz.getName());

               
                Object instance = null;
                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    System.out.println("   Nu s-a putut instantia clasa.");
                }


                for (Method method : clazz.getDeclaredMethods()) {
                    System.out.println("   Metoda: " + method.getReturnType().getSimpleName() + " " + method.getName() + "()");

                    boolean hasTargetAnnotation = false;
                    for (Class<?> annClass : annotationTypes) {
                        if (method.isAnnotationPresent(annClass.asSubclass(Annotation.class))) {
                            hasTargetAnnotation = true;
                            break;
                        }
                    }

                    if (hasTargetAnnotation) {
                        int paramCount = method.getParameterCount();
                        Class<?>[] paramTypes = method.getParameterTypes();

                        try {
                            if (paramCount == 0) {
                                System.out.println("      -> [!] Invocam metoda (Nu necesita argumente)...");
                                method.invoke(instance);

                            } else if (paramCount == 1 && (paramTypes[0] == int.class || paramTypes[0] == Integer.class)) {
                                int mockValue = 42;
                                System.out.println("      -> [!] Invocam metoda (Un argument int, folosim mock value: " + mockValue + ")...");
                                method.invoke(instance, mockValue);
                            }
                        } catch (Exception e) {
                            System.err.println("      -> [X] Eroare la invocare: " + e.getCause());
                        }
                    }
                }
                System.out.println("------------------------------------------");
            }
        }
    }
}