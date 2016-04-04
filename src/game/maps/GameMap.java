package game.maps;


import java.util.*;

/**
 * A Game map, which places objects at locations.
 * An object can only be at one location, and each location can only can contain one object
 * @param <T>
 * @param <U>
 */
public abstract class GameMap<T, U extends MapPoint> implements ReadonlyGameMap<T, U>{
    private final HashMap<U, T> items;
    private final HashMap<T, U> locations;
    public GameMap(){
        items = new HashMap<>();
        locations = new HashMap<>();
    }

    public T get(U point) {
        return items.get(point);
    }

    public U locationOf(T item){
        return locations.get(item);
    }

    public void put(T item, U point) {
        clear(point);
        remove(item);
        items.put(point, item);
        locations.put(item, point);
    }

    public T clear(U point){
        T item = items.remove(point);
        locations.remove(item);
        return item;
    }

    public U remove(T item){
        U location = locations.remove(item);
        items.remove(location);
        return location;
    }

    public boolean isNeighbor(U origin, U destination){
        return getNeighbors(origin).contains(destination);
    }

    public boolean itemAt(U point) {
        return items.containsKey(point);
    }

    public boolean itemAt(U point, T item){
        return items.get(point).equals(item);
    }

    public final boolean contains(T item){
        return locations.containsKey(item);
    }

    public T move(U location, U destination){
        T item = clear(location);
        put(item, destination);
        return item;
    }

    public HashMap<T, U> items(){
        return new HashMap<>(locations);
    }

    public HashMap<U, T> filledLocations(){
        return new HashMap<>(items);
    }

    public abstract List<U> getNeighbors(U point);

    /**
     * @param from Start point
     * @param to End point
     * @return True if there is a path between the two points, false otherwise.
     */
    public boolean isConnected(U from, U to){
        return isConnected(from, to, new HashSet<>());
    }

    private boolean isConnected(U from, U to, HashSet<U> visited){
        if (visited.contains(from)){
            return false;
        }
        visited.add(from);
        return getNeighbors(to).stream().anyMatch(i -> isConnected(i, to, visited));
    }

    /**
     * @param from Start point
     * @param to End point
     * @return The shortest path between the two points.  If the points are not connected, then an empty list will be returned.
     */
    public List<U> findPath(U from, U to){
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
