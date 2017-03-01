package com.nmerrill.kothcomm.game.maps.graphmaps.neighborhoods;

import com.nmerrill.kothcomm.game.maps.MapPoint;
import org.eclipse.collections.api.set.ImmutableSet;


public interface Neighborhood<U extends MapPoint> {
    ImmutableSet<U> getAdjacencies(U point);
}
