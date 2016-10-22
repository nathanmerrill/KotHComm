package com.ppcg.kothcomm.game.maps.generators;

import com.ppcg.kothcomm.game.maps.MapPoint;
import com.ppcg.kothcomm.game.maps.gridmaps.GridMap;
import com.ppcg.kothcomm.game.maps.gridmaps.adjacencies.AdjacencySet;
import com.ppcg.kothcomm.game.maps.gridmaps.bounds.Bounds;

import java.util.stream.Collectors;


public class Fill<T> implements Generator<GridMap<?, T>>{

    private final T filler;
    public Fill(T filler){
        this.filler = filler;
    }

    @Override
    public void generate(GridMap<?, T> map) {
        generateMap(map);
    }

    private <U extends MapPoint> void generateMap(GridMap<U, T> map){
        map.emptyLocations().forEach(p ->
                map.put(p, filler)
        );
    }
}
