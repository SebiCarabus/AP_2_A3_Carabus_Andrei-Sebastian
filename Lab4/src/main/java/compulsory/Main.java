package compulsory;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        var nodes= IntStream.rangeClosed(1,10)
                .mapToObj(i -> new Intersection("v"+i))
                .toArray(Intersection[]::new);

        List<Street> streets = new LinkedList<>();
        Set<Intersection> intersections = new HashSet<>();

        for(int i=0; i < nodes.length ;i++) {
            //System.out.println(nodes[i].toString());
            streets.add(new Street("s"+(i+1),nodes.length-i,nodes[i%nodes.length],nodes[(i+1)%nodes.length]));

            intersections.add(nodes[i]);
            intersections.add(nodes[i]);
            intersections.add(nodes[i]);
        }

        Collections.sort(streets,Street::compareTo);

        streets.forEach(street -> System.out.println(street.toString()));

        System.out.println("-------------------------------------------------------");

        intersections.forEach(intersection -> System.out.println(intersection.toString()));
    }
}
