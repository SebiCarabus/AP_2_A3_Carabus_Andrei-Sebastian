package compulsory;

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

        Company[] companies = new Company[2];
        companies[0]=new Company();
        companies[0].setName("Bit Defender");
        companies[1]=new Company();
        companies[1].setName("ASSIST");

        for(int i=0;i<2;i++){
            list.add(persons[i]);
            list.add(companies[i]);
        }

        Collections.sort(list);

        list.forEach(curProfile -> {
            System.out.print(curProfile.getId()+" -> ");
            System.out.println(curProfile.getName());
        });
    }
}
