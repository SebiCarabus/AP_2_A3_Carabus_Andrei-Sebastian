package advanced;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CoverCommand implements Command{
    private Catalog catalog;
    private List<String> targetConcepts = new ArrayList<>();
    @Override
    public void execute()throws Exception{
        List<Item> smallestConceptCover=this.findSmallestConceptCove();
        System.out.println("Smallest set of resources which cover the given concepts in the "+ this.catalog.getName() +" catalog: ");
        smallestConceptCover.forEach(item -> {
            System.out.println(item.getTitle());
        });
    }

    private List<Item> findSmallestConceptCove(){
        Set<String> uncoveredConcepts=new HashSet<>(this.targetConcepts);
        List<Item> selectedItems=new ArrayList<>();
        List<Item> availableItems =new ArrayList<>(this.catalog.getItems());

        while(!uncoveredConcepts.isEmpty()){
            Item bestAvailableItem=null;
            int maxCoveredConcepts=0;

            for(Item availableItem:availableItems){
                int coveredNewConcepts=(int)availableItem.getConcepts()
                        .stream()
                        .filter(uncoveredConcepts::contains)
                        .count();

                if(coveredNewConcepts>maxCoveredConcepts) {
                    maxCoveredConcepts = coveredNewConcepts;
                    bestAvailableItem = availableItem;
                }
            }

            if(bestAvailableItem==null){
                break;
            }

            selectedItems.add(bestAvailableItem);
            uncoveredConcepts.removeAll(bestAvailableItem.getConcepts());
            availableItems.remove(bestAvailableItem);
        }

        return selectedItems;
    }
}
