package com.nmerrill.kothcomm.game.maps.generators;

import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.game.maps.graphmaps.GraphGraphMap;
import com.nmerrill.kothcomm.game.maps.graphmaps.neighborhoods.Neighborhood;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Region;

public class FillRegion<U extends MapPoint> implements Generator<GraphGraphMap<U, ?>>{
    private final Neighborhood<U> neighborhood;
    private final Region<U> region;
    public FillRegion(Neighborhood<U> neighborhood, Region<U> region){
        this.neighborhood = neighborhood;
        this.region = region;
    }

    @Override
    public void generate(GraphGraphMap<U, ?> map) {
        addPoint(region.startingPoint(), map);
    }

    public void addPoint(U point, GraphGraphMap<U, ?> map){
        if (map.contains(point)){
            return;
        }
        map.put(point, null);
        neighborhood.getAdjacencies(point).select(region::inBounds).forEachWith(this::addPoint, map);
    }
}
