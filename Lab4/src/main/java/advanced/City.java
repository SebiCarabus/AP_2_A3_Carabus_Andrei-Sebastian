package advanced;

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

    public void solve2ApproxTSP() {
        Graph graph = this.buildGraphFromCityMap();
        Iterator iterator = new WeightedSpanningTreeIterator(graph, true);
        if (!iterator.hasNext()) {
            System.out.println("The TSP problem can't be solved since the city is not connected");
            return;
        }

        Set<Edge> mstEdges = (Set<Edge>) iterator.next();

        Graph mstGraph = GraphBuilder.empty().estimatedNumVertices(graph.numVertices()).buildGraph();

        for (int i=0; i<graph.numVertices(); i++) {
            mstGraph.addVertex(i);
        }
        for (Edge edge : mstEdges) {
            mstGraph.addEdge(edge.source(), edge.target());
            mstGraph.setEdgeWeight(edge.source(), edge.target(),edge.weight());
        }

        List<Integer> tour = new ArrayList<>();
        boolean[] visited = new boolean[graph.numVertices()];
        dfs(0, mstGraph, visited, tour);

        double totalCost = 0;
        Intersection startIntersection=null;
        for (int i = 0; i < tour.size(); i++) {
            int indexIntersection1 = tour.get(i);
            int indexIntersection2 = tour.get((i + 1) % tour.size());
            totalCost += graph.getEdgeWeight(indexIntersection1, indexIntersection2);

            Intersection curentIntersection = (Intersection) graph.getVertexLabel(indexIntersection1);
            if(i==0){
                startIntersection=curentIntersection;
            }
            System.out.print(curentIntersection.getName() + " -> ");
        }

        System.out.println(startIntersection.getName());
        System.out.println("Total cost < 2 * optimal: " + (int)totalCost);
    }

    private void dfs(int currentVertex, Graph mst, boolean[] visited, List<Integer> tour) {
        visited[currentVertex] = true;
        tour.add(currentVertex);
        for (int neighbor : mst.neighbors(currentVertex)) {
            if (!visited[neighbor]) {
                dfs(neighbor, mst, visited, tour);
            }
        }
    }
}
