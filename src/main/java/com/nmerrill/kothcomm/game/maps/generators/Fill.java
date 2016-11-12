package com.nmerrill.kothcomm.game.maps.generators;

import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.game.maps.graphmaps.GraphMap;


public class Fill<T> implements Generator<GraphMap<?, T>>{

    private final T filler;
    public Fill(T filler){
        this.filler = filler;
    }

    @Override
    public void generate(GraphMap<?, T> map) {
        generateMap(map);
    }

    private <U extends MapPoint> void generateMap(GraphMap<U, T> map){
        map.emptyLocations().forEach(p ->
                map.put(p, filler)
        );
    }
}
