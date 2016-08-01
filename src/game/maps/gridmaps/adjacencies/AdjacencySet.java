package game.maps.gridmaps.adjacencies;

import game.maps.MapPoint;
import game.maps.gridmaps.Vector;

import java.util.Set;

public interface AdjacencySet<U extends MapPoint> {
    Set<? extends Vector<U>> getAdjacencies(U point);
}
