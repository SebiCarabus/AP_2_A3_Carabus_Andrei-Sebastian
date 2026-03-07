package advanced;

import java.util.*;

class Edge {
    Profile u, v;
    Edge(Profile u, Profile v) { this.u = u; this.v = v; }
}
public class MaximalPart {
    private SocialNetwork network;
    private int timer=0;
    private Stack<Edge> stack = new Stack<>();
    private List<List<Edge>> biconnectedComponents = new ArrayList<>();

    public List<List<Edge>> getBiconnectedComponents() {
        return biconnectedComponents;
    }

    MaximalPart(SocialNetwork network){
        this.network=network;
    }
    private static boolean hasBidirectionalRelasionShips(Profile profile){
        for(Profile toProfile:profile.getRelationShips().keySet()){
            boolean ok=false;
            if(toProfile.getId()==profile.getId()){
                return false;
            }
            for(Profile backProfile:toProfile.getRelationShips().keySet()){
                if(backProfile.getId()==profile.getId()){
                    ok=true;
                    break;
                }
            }
            if(!ok){
                return false;
            }
        }
        return true;
    }

    private boolean isValid(){
        for(int i=0;i<this.network.getProfiles().size();i++){
            if(!hasBidirectionalRelasionShips(this.network.getProfiles().get(i))){
                return false;
            }
        }
        return true;
    }

    private void extractComponent(Profile u, Profile v){
        List<Edge> component = new ArrayList<>();
        while(true){
            Edge edge = stack.pop();
            component.add(edge);
            if(edge.u == u && edge.v.equals(v)) {
                break;
            }
        }
        this.biconnectedComponents.add(component);
    }
    private void findBCC(Profile current, Profile before, Map<Profile,Boolean> visited, Map<Profile,Integer> time, Map<Profile,Integer> lowestTime){
        visited.put(current, true);
        time.put(current, this.timer);
        lowestTime.put(current, this.timer);
        this.timer++;
        int children = 0;
        for(Profile nextProfile : current.getRelationShips().keySet()){
            if(before != null && nextProfile.getId() == before.getId()){
                continue;
            }
            if(visited.get(nextProfile)){
                lowestTime.put(current, Math.min(lowestTime.get(current), time.get(nextProfile)));
                if(time.get(nextProfile)<time.get(current)){
                    this.stack.push(new Edge(current,nextProfile));
                }
            } else {
                children++;
                this.stack.push(new Edge(current,nextProfile));
                findBCC(nextProfile, current, visited, time, lowestTime);

                lowestTime.put(current, Math.min(lowestTime.get(current), lowestTime.get(nextProfile)));

                if((before == null && children > 1)||(before != null && lowestTime.get(nextProfile) >= time.get(current))){
                    extractComponent(current,nextProfile);
                }
            }
        }
    }
    public void showSolution(){
        if(!this.isValid()){
            System.out.println("We cannot resolve the problem since the given network is not bidirectional");
            return;
        }
        this.biconnectedComponents.clear();
        this.stack.clear();
        this.timer=0;
        Map<Profile,Boolean> visited=new HashMap<>();
        Map<Profile,Integer> time=new HashMap<>();
        Map<Profile,Integer> lowestTime=new HashMap<>();
        this.network.getProfiles().forEach(profile -> {
            visited.put(profile,false);
            time.put(profile,-1);
            lowestTime.put(profile,-1);
        });

        for(int i=0;i<this.network.getProfiles().size();i++){
            Profile current=this.network.getProfiles().get(i);
            if(!visited.get(current)){
                this.findBCC(current,null,visited,time,lowestTime);

                if(!this.stack.empty()){
                    List<Edge> component = new ArrayList<>();
                    while(!stack.isEmpty()){
                        component.add(stack.pop());
                    }
                    this.biconnectedComponents.add(component);
                }
            }
        }

        System.out.println("Maximal parts (Biconnected Components):");
        for(int i=0; i<this.biconnectedComponents.size();i++){
            System.out.println("Component number "+(i+1)+": ");
            Set<String> nodes=new HashSet<>();
            for(Edge edge: biconnectedComponents.get(i)){
                nodes.add(edge.u.getName());
                nodes.add(edge.v.getName());
            }
            System.out.println(nodes);
        }

    }
}
