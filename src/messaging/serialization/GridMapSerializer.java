package messaging.serialization;


import game.maps.MapPoint;
import game.maps.gridmaps.GridMap;

import java.util.function.Function;
import java.util.function.Supplier;

public class GridMapSerializer<U extends MapPoint, T> implements Serializer<GridMap<U, T>> {

    private final Function<Character, T> reverse;
    private final Function<T, Character> mapping;
    private final Supplier<GridMap<U, T>> supplier;
    public GridMapSerializer(Function<T, Character> mapping, Function<Character, T> reverse, Supplier<GridMap<U,T>> supplier){
        this.mapping = mapping;
        this.reverse = reverse;
        this.supplier = supplier;
    }

    @Override
    public GridMap<U, T> deserialize(String representation) {
        GridMap<U, T> map = supplier.get();
        int index = 0;
        for (U location: map.locations()){
            char character = representation.charAt(index);
            if (character == ' '){
                map.put(location, null);
            } else {
                map.put(location, reverse.apply(character));
            }
            index++;
        }
        return map;
    }

    @Override
    public String serialize(GridMap<U, T> map) {
        StringBuilder serialization = new StringBuilder();
        for (U location: map.locations()){
            T item = map.get(location);
            if (item == null){
                serialization.append(" ");
            } else {
                serialization.append(mapping.apply(item));
            }
        }
        return serialization.toString();
    }
}
