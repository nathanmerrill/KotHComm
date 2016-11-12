package com.nmerrill.kothcomm.game.maps.graphmaps.adjacencies;

import com.nmerrill.kothcomm.game.maps.MapPoint;
import org.eclipse.collections.api.set.MutableSet;


public interface AdjacencySet<U extends MapPoint> {
    MutableSet<U> getAdjacencies(U point);
}
