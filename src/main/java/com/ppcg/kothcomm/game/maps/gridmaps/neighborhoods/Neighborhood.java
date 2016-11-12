package com.ppcg.kothcomm.game.maps.gridmaps.neighborhoods;

import com.ppcg.kothcomm.game.maps.MapPoint;
import org.eclipse.collections.api.set.MutableSet;


public interface Neighborhood<U extends MapPoint> {
    MutableSet<U> getNeighborhood(U point);
}
