package game.maps.gridmaps.adjacencies;

import game.maps.gridmaps.GridPoint;
import game.maps.gridmaps.GridVector;

import java.util.List;

public interface AdjacencySet {
    List<GridVector> getAdjacencies(GridPoint point);
}
