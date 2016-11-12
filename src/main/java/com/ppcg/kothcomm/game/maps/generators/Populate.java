package com.ppcg.kothcomm.game.maps.generators;

import com.ppcg.kothcomm.game.maps.MapPoint;
import com.ppcg.kothcomm.game.maps.gridmaps.GridMap;
import com.ppcg.kothcomm.game.maps.gridmaps.neighborhoods.Neighborhood;
import com.ppcg.kothcomm.game.maps.gridmaps.bounds.Bounds;

public class Populate<U extends MapPoint> implements Generator<GridMap<U, ?>>{
    private final Neighborhood<U> adjacencySet;
    private final Bounds<U> bounds;
    public Populate(Neighborhood<U> adjacencySet, Bounds<U> bounds){
        this.adjacencySet = adjacencySet;
        this.bounds = bounds;
    }
    @Override
    public void generate(GridMap<U, ?> map) {

    }
}
