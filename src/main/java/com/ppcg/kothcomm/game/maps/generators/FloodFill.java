package com.ppcg.kothcomm.game.maps.generators;

import com.ppcg.kothcomm.game.maps.MapPoint;
import com.ppcg.kothcomm.game.maps.gridmaps.GridMap;

import java.util.Objects;

public class FloodFill<T, U extends MapPoint> implements Generator<GridMap<U, T>> {

    private final U startingLocation;
    private final T filler;
    public FloodFill(T filler, U startingLocation){
        this.startingLocation = startingLocation;
        this.filler = filler;
    }

    @Override
    public void generate(GridMap<U, T> map) {
        fillSpot(map, map.get(startingLocation), startingLocation);
    }

    private void fillSpot(GridMap<U, T> map, T match, U spot){
        if (!Objects.equals(map.get(spot), match)){
            return;
        }
        map.put(spot, filler);
        map.getNeighbors(spot).forEach(newSpot -> fillSpot(map, match, newSpot));
    }
}
