package homework;
import org.graph4j.Edge;
import org.graph4j.Graph;

import java.util.Set;
public record SpanningTreeSolution (
        int rank,
        Graph graph,
        Set<Edge> edges,
        int totalCost
){
    @Override
    public String toString() {
        StringBuilder stringBuilder=new StringBuilder();
        for (var edge : edges) {
            Street street = (Street) edge.label();
            int weight = (int)this.graph.getEdgeWeight(edge);
            stringBuilder.append("Street: " + street.getName() +
                    " | Length: " + weight +
                    " | Connects: " + street.getIntersection1().getName() +
                    " and " + street.getIntersection2().getName()+"\n");
        }
        stringBuilder.append("Total cost: "+this.totalCost+"\n");
        return stringBuilder.toString();
    }
}


