package game.maps.gridmaps.adjacencies;

import game.maps.gridmaps.GridPoint;
import game.maps.gridmaps.GridVector;

import java.util.ArrayList;
import java.util.List;

public final class SquareAdjacencySet implements AdjacencySet{
    @Override
    public List<GridVector> getAdjacencies(GridPoint point) {
        List<GridVector> points = new ArrayList<>();
        points.add(new GridVector(-1, 0));
        points.add(new GridVector(0, -1));
        points.add(new GridVector(0, 1));
        points.add(new GridVector(1, 0));
        return points;
    }
}
