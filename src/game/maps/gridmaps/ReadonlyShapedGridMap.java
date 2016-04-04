package game.maps.gridmaps;

import game.maps.ReadonlyBoundedMap;
import game.maps.gridmaps.adjacencies.AdjacencySet;

public interface ReadonlyShapedGridMap<T> extends ReadonlyBoundedMap<T, GridPoint> {

    AdjacencySet getAdjacencies();
    int getHeight();
    int minX(int y);
    int maxX(int y);
}
