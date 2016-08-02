package KoTHComm.game.maps.gridmaps.adjacencies;

import KoTHComm.game.maps.MapPoint;
import KoTHComm.game.maps.gridmaps.Vector;

import java.util.Set;

public interface AdjacencySet<U extends MapPoint> {
    Set<? extends Vector<U>> getAdjacencies(U point);
}
