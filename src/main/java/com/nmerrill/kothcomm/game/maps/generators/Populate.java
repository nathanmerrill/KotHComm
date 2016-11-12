package com.nmerrill.kothcomm.game.maps.generators;

import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.game.maps.graphmaps.GraphMapImpl;
import com.nmerrill.kothcomm.game.maps.graphmaps.adjacencies.AdjacencySet;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Bounds;

public class Populate<U extends MapPoint> implements Generator<GraphMapImpl<U, ?>>{
    private final AdjacencySet<U> adjacencySet;
    private final Bounds<U> bounds;
    private final U startingPoint;
    public Populate(AdjacencySet<U> adjacencySet, Bounds<U> bounds, U startingPoint){
        this.adjacencySet = adjacencySet;
        this.bounds = bounds;
        this.startingPoint = startingPoint;
    }

    @Override
    public void generate(GraphMapImpl<U, ?> map) {
        addPoint(startingPoint, map);
    }

    public void addPoint(U point, GraphMapImpl<U, ?> map){
        if (map.contains(point)){
            return;
        }
        map.put(point, null);
        adjacencySet.getAdjacencies(point).select(bounds::inBounds).forEachWith(this::addPoint, map);
    }
}
