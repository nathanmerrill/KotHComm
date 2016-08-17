package com.ppcg.kothcomm.game.maps.gridmaps.adjacencies;

import com.ppcg.kothcomm.game.maps.MapPoint;
import com.ppcg.kothcomm.game.maps.gridmaps.Vector;

import java.util.Set;

public interface AdjacencySet<U extends MapPoint> {
    Set<? extends Vector<U>> getAdjacencies(U point);
}
