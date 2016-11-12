package com.nmerrill.kothcomm.game.maps.generators;

import com.nmerrill.kothcomm.game.maps.graphmaps.GraphMap;
import com.nmerrill.kothcomm.game.maps.MapPoint;

import java.util.Objects;

public class FloodFill<T, U extends MapPoint> implements Generator<GraphMap<U, T>> {

    private final U startingLocation;
    private final T filler;
    public FloodFill(T filler, U startingLocation){
        this.startingLocation = startingLocation;
        this.filler = filler;
    }

    @Override
    public void generate(GraphMap<U, T> map) {
        fillSpot(map, map.get(startingLocation), startingLocation);
    }

    private void fillSpot(GraphMap<U, T> map, T match, U spot){
        if (!Objects.equals(map.get(spot), match)){
            return;
        }
        map.put(spot, filler);
        map.getNeighbors(spot).forEach(newSpot -> fillSpot(map, match, newSpot));
    }
}
