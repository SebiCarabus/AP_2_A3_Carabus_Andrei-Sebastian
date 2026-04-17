package advanced.services;

import advanced.domain.Movie;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieContraintService {
    public List<Movie> findUnrelatedMovies(List<Movie> allMovies, Map<Integer, Set<Integer>> adjacency, int minSize){
        Model model = new Model("Unrelated Movies Selection");

        int n = allMovies.size();
        BoolVar[] selection = model.boolVarArray("select",n);

        for(int i=0;i<n-1;i++){
            for(int j=i+1;j<n;j++){
                int id1=allMovies.get(i).getId();
                int id2=allMovies.get(j).getId();

                if(adjacency.getOrDefault(id1, Collections.emptySet()).contains(id2)){
                    model.arithm(selection[i],"+",selection[j],"<=",1);
                }
            }
        }

        model.sum(selection,">=",minSize).post();

        if(model.getSolver().solve()){
            List<Movie> result = new ArrayList<>();
            for(int i=0;i<n;i++){
                if(selection[i].getValue()==1) {
                    result.add(allMovies.get(i));
                }
            }
            return result;
        }

        return Collections.emptyList();
    }
}
