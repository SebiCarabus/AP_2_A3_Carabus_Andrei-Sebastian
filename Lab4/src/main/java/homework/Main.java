package homework;

import com.github.javafaker.Faker;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        Faker faker = new Faker();

        var nodes= IntStream.rangeClosed(1,10)
                .mapToObj(i -> new Intersection(faker.address().cityName()))
                .toArray(Intersection[]::new);

        List<Street> streets = new LinkedList<>();
        Set<Intersection> intersections = new HashSet<>();
        Map<Intersection, List<Street>> problemRepresentation= new HashMap<>();

        for(int i=0; i < nodes.length ;i++) {
            streets.add(new Street(faker.address().streetName(),nodes.length-i,nodes[i%nodes.length],nodes[(i+1)%nodes.length]));
        }

        for (Intersection node : nodes) {
            problemRepresentation.put(node, new LinkedList<>());
        }

        streets.forEach(street -> {
            problemRepresentation.get(street.getIntersection1()).add(street);
            problemRepresentation.get(street.getIntersection2()).add(street);
        });

        streets.stream()
                .filter(street -> street.getLength()>5)
                .collect(Collectors.toList())
                .forEach(street -> System.out.println(street.toString()));

        System.out.println();

        City city=new City(problemRepresentation);
        city.getCheapestTrees(3).ifPresentOrElse(
                solutions -> {
                    for (var sol : solutions) {
                        System.out.println("------MST number "+sol.rank()+"-----------\n"+sol.toString());
                    }
                },
                () -> System.out.println("No MST found!")
        );
    }
}
