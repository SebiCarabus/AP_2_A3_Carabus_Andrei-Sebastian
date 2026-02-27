import java.sql.SQLOutput;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
void main() {
    //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
    // to see how IntelliJ IDEA suggests fixing it.
    Location[] sites=new Location[3];
    sites[0]=new City("Suceava");
    sites[0].setX(10);
    sites[0].setY(20);
    sites[1]=new AirPort("Iasi");
    sites[1].setX(4);
    sites[1].setY(5);
    sites[2]=new GasStation("Rompetrol");
    sites[2].setX(0);
    sites[2].setY(2);

    System.out.println(sites[0].toString());
    System.out.println(sites[1].toString());
    System.out.println(sites[2].toString());
    System.out.println();

    Road[] roads=new Road[3];
    roads[0]=new Road("E19",90,RoadType.EXPRESS,sites[0],sites[1]);
    roads[1]=new Road("C32",60,RoadType.COUNTRY,sites[0],sites[2]);
    roads[2]=new Road("E24",100,RoadType.EXPRESS,sites[1],sites[2]);

    System.out.println(roads[0].toString());
    System.out.println(roads[1].toString());
    System.out.println(roads[2].toString());
    System.out.println();

    if(sites[0].equals(sites[0])){
        System.out.println(sites[0].toString() +" is equal with "+ sites[0].toString());
    } else {
        System.out.println(sites[0].toString() +" is not equal with "+ sites[0].toString());
    }

    if(sites[0].equals(sites[1])){
        System.out.println(sites[0].toString() +" is equal with "+ sites[1].toString());
    } else {
        System.out.println(sites[0].toString() +" is not equal with "+ sites[1].toString());
    }

    if(roads[0].equals(roads[0])){
        System.out.println(roads[0].toString() +" is equal with "+ roads[0].toString());
    } else {
        System.out.println(roads[0].toString() +" is not equal with "+ roads[0].toString());
    }

    if(roads[0].equals(roads[1])){
        System.out.println(roads[0].toString() +" is equal with "+ roads[1].toString());
    } else {
        System.out.println(roads[0].toString() +" is not equal with "+ roads[1].toString());
    }

    Problem problem=new Problem(sites,roads);
    if(problem.isValid()){
        System.out.println();
        System.out.println("The problem is valid!");
        System.out.println();

        Algorithm algorithm1=new IsPath(problem,sites[1],sites[2]);
        Solution sol=algorithm1.solve();
        System.out.println(sol.toString());
        System.out.println();

        Algorithm algorithm2=new Dijkstra(problem,sites[1],sites[2]);
        sol=algorithm2.solve();
        System.out.println(sol.toString());
    } else {
        System.out.println();
        System.out.println("The problem is valid!");
        System.out.println();
    }
}
