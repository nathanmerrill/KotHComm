package game.maps.gridmaps;


import game.maps.GameMap;
import game.maps.MapPoint;

import java.util.*;
import java.util.stream.Stream;


/**
 * A map with a finite number of locations and connections between those locations
 * @param <U>
 * @param <T>
 */
public class GridMap<U extends MapPoint, T>
        implements GameMap<U, T>, ReadonlyGridMap<U, T> {

    private final HashMap<U, Set<U>> connections;
    private final HashMap<U, T> items;

    public GridMap(){
        connections = new HashMap<>();
        items = new HashMap<>();
    }

    @Override
    public boolean outOfBounds(U point) {
        return items.containsKey(point);
    }

    @Override
    public Set<U> getNeighbors(U origin) {
        return new HashSet<>(connections.get(origin));
    }

    @Override
    public T get(U point) {
        return items.get(point);
    }

    @Override
    public T clear(U point) {
        T item = items.remove(point);
        if (item != null) {
            connections.remove(point)
                    .forEach(connection -> connections.get(connection).remove(point));
        }
        return item;
    }

    public void connect(U point, U neighbor){
        connections.get(point).add(neighbor);
        connections.get(neighbor).add(point);
    }

    public void disconnect(U point, U neighbor){
        connections.get(point).remove(neighbor);
        connections.get(neighbor).remove(point);
    }

    @Override
    public Iterator<U> iterator() {
        return locations().iterator();
    }

    @Override
    public void put(U point, T item) {
        items.put(point, item);
        connections.putIfAbsent(point, new HashSet<>());
    }

    @Override
    public boolean contains(U point) {
        return items.containsKey(point);
    }

    @Override
    public HashMap<U, T> toMap() {
        return new HashMap<>(items);
    }

    @Override
    public boolean contains(U point, T item) {
        //noinspection SimplifiableIfStatement
        if (contains(point)){
            return false;
        }
        return Objects.equals(items.get(point), item);
    }

    @Override
    public Stream<U> stream() {
        return locations().stream();
    }

    @Override
    public Set<U> locations() {
        return new HashSet<>(items.keySet());
    }

    @Override
    public Set<U> emptyLocations() {
        Set<U> locations = locations();
        locations.removeIf(this::isFilled);
        return locations;
    }

    @Override
    public Set<U> filledLocations() {
        Set<U> locations = locations();
        locations.removeIf(this::isEmpty);
        return locations;
    }

    @Override
    public boolean isFilled(U point) {
        return items.get(point) != null;
    }

    @Override
    public boolean isEmpty(U point) {
        //noinspection SimplifiableIfStatement
        if (!contains(point)){
            return false;
        }
        return items.get(point) == null;
    }

    @Override
    public boolean isNeighbor(U origin, U neighbor) {
        return connections.get(origin).contains(neighbor);
    }

    @Override
    public List<T> items() {
        return new ArrayList<>(items.values());
    }
}
