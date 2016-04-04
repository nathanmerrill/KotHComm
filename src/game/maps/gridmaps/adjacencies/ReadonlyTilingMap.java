package game.maps.gridmaps.adjacencies;

import game.maps.ReadonlyBoundedMap;
import game.maps.gridmaps.GridPoint;

public interface ReadonlyTilingMap<T> extends ReadonlyBoundedMap<T, GridPoint> {
    int getWidth();
    int getHeight();
    AdjacencySet getAdjacencies();
}
