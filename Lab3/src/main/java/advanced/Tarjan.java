package advanced;

import java.util.*;

public class Tarjan {
    private SocialNetwork network;
    private int timer=0;
    List<String> cutPoints = new ArrayList<>();
    Tarjan(SocialNetwork network){
        this.network=network;
    }

    public List<String> getCutPoints() {
        return cutPoints;
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
    private void tarjan(Profile current, Profile before, Map<Profile,Boolean> visited, Map<Profile,Integer> time, Map<Profile,Integer> lowestTime){
        visited.put(current, true);
        time.put(current, this.timer);
        lowestTime.put(current, this.timer);
        this.timer++;
        int children = 0;
        boolean isArticulationPoint = false;
        for(Profile nextProfile : current.getRelationShips().keySet()){
            if(before != null && nextProfile.getId() == before.getId()){
                continue;
            }
            if(visited.get(nextProfile)){
                lowestTime.put(current, Math.min(lowestTime.get(current), time.get(nextProfile)));
            } else {
                children++;
                tarjan(nextProfile, current, visited, time, lowestTime);

                lowestTime.put(current, Math.min(lowestTime.get(current), lowestTime.get(nextProfile)));

                if(before != null && lowestTime.get(nextProfile) >= time.get(current)){
                    isArticulationPoint = true;
                }
            }
        }
        if(before == null && children > 1){
            isArticulationPoint = true;
        }
        if(isArticulationPoint){
            this.cutPoints.add(current.getName());
        }
    }
    public void showSolution(){
        if(!this.isValid()){
            System.out.println("We cannot resolve the problem since the given network is not bidirectional");
            return;
        }
        this.cutPoints.clear();
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
                this.tarjan(current,null,visited,time,lowestTime);
            }
        }

        if(this.cutPoints.size()==0){
            System.out.println("In the given network don't exist cut points!");
        } else {
            System.out.println("The cutting points of the network are:");
            System.out.println(this.cutPoints);
        }
    }
}
