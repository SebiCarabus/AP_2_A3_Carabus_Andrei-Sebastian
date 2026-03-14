package homework;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.graph4j.Edge;
import org.graph4j.Graph;
import org.graph4j.GraphBuilder;
import org.graph4j.spanning.WeightedSpanningTreeIterator;

import java.util.*;

@AllArgsConstructor
@Getter
@Setter
public class City {
    private Map<Intersection, List<Street>> cityMap = new HashMap<>();

    private Graph buildGraphFromCityMap(){
        Graph graph = GraphBuilder.empty()
                .estimatedNumVertices(this.cityMap.size())
                .buildGraph();

        Map<Intersection,Integer> graphVertexAsosiacion = new HashMap<>();

        int i=0;
        for(Intersection intersection:this.cityMap.keySet()){
            graphVertexAsosiacion.put(intersection,new Integer(i));
            i++;
        }

        i=0;
        for(Intersection intersection:this.cityMap.keySet()){
            graph.addLabeledVertex(i,intersection);
            i++;
        }

        for(var intersectionStreets:this.cityMap.entrySet()){
            for(Street street:intersectionStreets.getValue()){
                var intersection1=street.getIntersection1();
                var intersection2=street.getIntersection2();
                double weight=street.getLength();
                graph.addLabeledEdge(graphVertexAsosiacion.get(intersection1).intValue(),graphVertexAsosiacion.get(intersection2).intValue(),street);
                graph.setEdgeWeight(graphVertexAsosiacion.get(intersection1).intValue(),graphVertexAsosiacion.get(intersection2).intValue(),weight);
                //graph.addLabeledEdge(graphVertexAsosiacion.get(intersection2).intValue(),graphVertexAsosiacion.get(intersection1).intValue(),street);
                //graph.setEdgeWeight(graphVertexAsosiacion.get(intersection2).intValue(),graphVertexAsosiacion.get(intersection1).intValue(),weight);
            }
        }

        return graph;
    }
    public Optional<List<SpanningTreeSolution>> getCheapestTrees(int numberOfSolutions){
        Graph graph = this.buildGraphFromCityMap();
        List<SpanningTreeSolution> solutions = new ArrayList<>();
        int i=0;
        for(Iterator iterator=new WeightedSpanningTreeIterator(graph,true);iterator.hasNext() && i<numberOfSolutions ;i++){
            Set<Edge> edges = (Set<Edge>) iterator.next();
            int currentCost = 0;

            for (var edge : edges) {
                Street street = (Street) edge.label();
                int weight = (int)graph.getEdgeWeight(edge);
                currentCost += weight;
            }

            solutions.add(new SpanningTreeSolution((i+1),graph,edges,currentCost));
        }

        return solutions.isEmpty() ? Optional.empty() : Optional.of(solutions);
    }
}
