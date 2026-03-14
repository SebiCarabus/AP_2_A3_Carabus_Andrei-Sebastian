package advanced;

import com.github.javafaker.Faker;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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

        for(int i=0; i < nodes.length-1 ;i++) {
            for(int j=i+1; j < nodes.length ;j++){
                int randomLength = ThreadLocalRandom.current().nextInt(1, 100);
                streets.add(new Street(faker.address().streetName(),randomLength,nodes[i],nodes[j]));
            }
        }

        for (Intersection node : nodes) {
            problemRepresentation.put(node, new LinkedList<>());
        }

        streets.forEach(street -> {
            problemRepresentation.get(street.getIntersection1()).add(street);
            problemRepresentation.get(street.getIntersection2()).add(street);
        });

        City city=new City(problemRepresentation);
        city.solve2ApproxTSP();
    }
}
