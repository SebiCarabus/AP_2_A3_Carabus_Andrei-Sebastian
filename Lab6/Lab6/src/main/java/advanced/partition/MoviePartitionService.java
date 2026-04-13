package advanced.partition;

import advanced.objects.Movie;
import java.util.*;

public class MoviePartitionService {

    public List<List<Movie>> partition(List<Movie> allMovies, Map<Integer, Set<Integer>> adjacency) {
        allMovies.sort((movie1, movie2) -> Integer.compare(
                adjacency.getOrDefault(movie2.getId(), Collections.emptySet()).size(),
                adjacency.getOrDefault(movie1.getId(), Collections.emptySet()).size()
        ));

        Map<Integer, Integer> movieColors = new HashMap<>();
        int maxColor = 0;

        for (Movie movie : allMovies) {
            Set<Integer> neighborColors = new HashSet<>();
            for (Integer neighborId : adjacency.getOrDefault(movie.getId(), Collections.emptySet())) {
                if (movieColors.containsKey(neighborId)) {
                    neighborColors.add(movieColors.get(neighborId));
                }
            }

            int color = 0;
            while (neighborColors.contains(color)) {
                color++;
            }
            movieColors.put(movie.getId(), color);
            maxColor = Math.max(maxColor, color);
        }

        List<List<Movie>> result = new ArrayList<>();
        for (int i = 0; i <= maxColor; i++) {
            result.add(new ArrayList<>());
        }
        for (Movie movie : allMovies) {
            result.get(movieColors.get(movie.getId())).add(movie);
        }

        return balancePartitions(result, adjacency);
    }

    private List<List<Movie>> balancePartitions(List<List<Movie>> partitions, Map<Integer, Set<Integer>> adjacency) {
        boolean changed = true;
        while (changed) {
            changed = false;
            partitions.sort(Comparator.comparingInt(List::size));
            List<Movie> largest = partitions.get(partitions.size() - 1);
            List<Movie> smallest = partitions.get(0);

            if (largest.size() - smallest.size() > 1) {
                for (int i = 0; i < largest.size(); i++) {
                    Movie movie = largest.get(i);
                    if (canPlaceInList(movie, smallest, adjacency)) {
                        largest.remove(i);
                        smallest.add(movie);
                        changed = true;
                        break;
                    }
                }
            }
        }
        return partitions;
    }

    private boolean canPlaceInList(Movie movie, List<Movie> list, Map<Integer, Set<Integer>> adjacency) {
        Set<Integer> neighbors = adjacency.getOrDefault(movie.getId(), Collections.emptySet());
        for (Movie other : list) {
            if (neighbors.contains(other.getId())) {
                return false;
            }
        }
        return true;
    }
}