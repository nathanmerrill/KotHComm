package game.serialization;


import game.maps.BoundedMap;
import game.maps.MapPoint;
import game.maps.gridmaps.GridMap;
import game.maps.gridmaps.TilingMap;
import game.maps.gridmaps.adjacencies.AdjacencySet;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BoundedMapSerializer<T, U extends MapPoint> implements Serializer<BoundedMap<T, U>> {

    private final Map<Character, T> mapping;
    private final Map<T, Character> reverseMapping;
    private final Supplier<BoundedMap<T, U>> supplier;
    public BoundedMapSerializer(Map<Character, T> mapping, Supplier<BoundedMap<T, U>> supplier){
        this.mapping = mapping;
        reverseMapping = mapping.keySet().stream().collect(Collectors.toMap(mapping::get, Function.identity()));
        this.supplier = supplier;
    }

    @Override
    public int separationLevel() {
        return 0;
    }

    @Override
    public BoundedMap<T, U> deserialize(String representation) {
        BoundedMap<T, U> map = supplier.get();
        int index = 0;
        for (U location: map.items().keySet()){
            map.put(location, mapping.get(representation.charAt(index)));
            index++;
        }
        return map;
    }

    @Override
    public String define() {
        BoundedMap<T, U> map = supplier.get();
        StringBuilder definition = new StringBuilder();
        if (map instanceof TilingMap) {
            TilingMap<T> tilingMap = (TilingMap<T>) map;
            definition.append("Map(").append(defineAdjacency(tilingMap.getAdjacencies())).append(",");
            definition.append(tilingMap.getWidth()).append(",").append(tilingMap.getHeight()).append(")");
        }
        if (map instanceof GridMap) {
            GridMap<T, ?> gridMap = (GridMap<T, ?>) map;
            definition.append("ShapeMap(").append(defineAdjacency(gridMap.getAdjacencies())).append(",");
            definition.append(gridMap.getHeight());
            for (int i = 0; i < gridMap.getHeight(); i++){
                definition.append(",").append(gridMap.minX(i)).append(",").append(gridMap.maxX(i));
            }
            definition.append(")");
        }
        return definition.toString();
    }

    private String defineAdjacency(AdjacencySet set){
        String name = set.getClass().getSimpleName();
        return name.substring(0, name.indexOf("AdjacencySet"));
    }

    @Override
    public String serialize(BoundedMap<T, U> value) {
        StringBuilder serialization = new StringBuilder();
        for (U location: value.items().keySet()){
            T item = value.get(location);
            if (item == null){
                serialization.append(" ");
            } else {
                serialization.append(reverseMapping.get(item));
            }
        }
        return serialization.toString();
    }
}
