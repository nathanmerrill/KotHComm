package com.ppcg.kothcomm.game.maps.gridmaps.adjacencies;

import com.ppcg.kothcomm.game.maps.MapPoint;
import org.eclipse.collections.api.set.MutableSet;


public interface AdjacencySet<U extends MapPoint> {
    MutableSet<U> getAdjacencies(U point);
}
