package homework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        List<Profile> list = new ArrayList<>();

        Person[] persons = new Person[2];
        persons[0] = new Person();
        persons[0].setName("Arseni Radu");
        persons[1] = new Person();
        persons[1].setName("Covita Sebastian");
        persons[1].addRelationShip(persons[0],"Best Friend");

        Company[] companies = new Company[2];
        companies[0]=new Company();
        companies[0].setName("Bit Defender");
        companies[0].addRelationShip(persons[1],"Emplyee");
        companies[1]=new Company();
        companies[1].setName("ASSIST");
        companies[0].addRelationShip(companies[1],"Under");

        for(int i=0;i<2;i++){
            list.add(persons[i]);
            list.add(companies[i]);
        }

        SocialNetwork network=new SocialNetwork();
        list.forEach(curProfile -> {
            network.addProfile(curProfile);
        });

        network.print();
    }
}
