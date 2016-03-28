package game.maps;


import java.util.HashMap;
import java.util.List;

public abstract class GameMap<T, U extends MapPoint> {
    private final HashMap<U, T> items;
    private final HashMap<T, U> locations;
    public GameMap(){
        items = new HashMap<>();
        locations = new HashMap<>();
    }

    public T get(U point) {
        return items.get(point);
    }

    public void put(U point, T item) {
        items.put(point, item);
        locations.put(item, point);
    }

    public boolean contains(U point) {
        return items.containsKey(point);
    }

    public T remove(U point){
        T item = items.remove(point);
        locations.remove(item);
        return item;
    }

    public T move(U location, U destination){
        T item = items.remove(location);
        items.put(destination, item);
        locations.put(item, destination);
        return item;
    }

    public U move(T item, U destination){
        U location = locations.get(item);
        items.remove(destination);
        items.put(location, item);
        locations.put(item, destination);
        return location;
    }

    public U getPosition(T item){
        return locations.get(item);
    }

    public HashMap<T, U> items(){
        return new HashMap<>(locations);
    }

    public HashMap<U, T> locations(){
        return new HashMap<>(items);
    }

    public abstract List<U> getNeighbors(U point);
}
