package com.nmerrill.kothcomm.game.maps.graphmaps.neighborhoods;

import com.nmerrill.kothcomm.game.maps.MapPoint;
import org.eclipse.collections.api.set.MutableSet;


public interface Neighborhood<U extends MapPoint> {
    MutableSet<U> getAdjacencies(U point);
}
