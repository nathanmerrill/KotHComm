package game.maps.gridmaps;


import game.maps.MapPoint;
import game.maps.ReadonlyGameMap;

import java.util.*;
import java.util.stream.Stream;

public interface ReadonlyGridMap<U extends MapPoint, T> extends ReadonlyGameMap<U, T>, Iterable<U>{

    Set<U> getNeighbors(U origin);
    boolean isNeighbor(U origin, U neighbor);
    boolean isFilled(U point);
    boolean isEmpty(U point);
    Set<U> locations();
    Set<U> emptyLocations();
    Set<U> filledLocations();
    List<T> items();
    Stream<U> stream();

    /**
     * @param from Start point
     * @param to End point
     * @return True if there is a path between the two points, false otherwise.
     */
    default boolean isPath(U from, U to){
        return shortestPath(from, to).isEmpty();
    }

    /**
     * @param from Start point
     * @param to End point
     * @return The shortest path between the two points.  If the points are not connected, then an empty list will be returned.
     */
    default List<U> shortestPath(U from, U to){
        HashMap<U, List<U>> paths = new HashMap<>();
        Queue<U> toVisit = new LinkedList<>();
        toVisit.add(from);
        paths.put(from, new ArrayList<>());
        while (!toVisit.isEmpty()){
            U node = toVisit.poll();
            List<U> path = new ArrayList<>(paths.get(node));
            path.add(node);
            if (node.equals(to)){
                return path;
            }
            getNeighbors(node).forEach(i->paths.putIfAbsent(i, new ArrayList<>(path)));
        }
        return new ArrayList<>();
    }

}
