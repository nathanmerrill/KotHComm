package com.ppcg.kothcomm.game.maps.generators;

import com.ppcg.kothcomm.game.maps.MapPoint;
import com.ppcg.kothcomm.game.maps.gridmaps.GridMap;
import com.ppcg.kothcomm.game.maps.gridmaps.adjacencies.AdjacencySet;
import com.ppcg.kothcomm.game.maps.gridmaps.bounds.Bounds;

public class Populate<U extends MapPoint> implements Generator<GridMap<U, ?>>{
    private final AdjacencySet<U> adjacencySet;
    private final Bounds<U> bounds;
    public Populate(AdjacencySet<U> adjacencySet, Bounds<U> bounds){
        this.adjacencySet = adjacencySet;
        this.bounds = bounds;
    }
    @Override
    public void generate(GridMap<U, ?> map) {

    }
}
