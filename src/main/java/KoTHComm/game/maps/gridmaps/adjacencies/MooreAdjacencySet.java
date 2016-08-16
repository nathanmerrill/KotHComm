package KoTHComm.game.maps.gridmaps.adjacencies;

import KoTHComm.game.maps.gridmaps.Point2D;
import KoTHComm.game.maps.gridmaps.Vector2D;

import java.util.HashSet;
import java.util.Set;

public final class MooreAdjacencySet implements AdjacencySet<Point2D> {
    @Override
    public Set<Vector2D> getAdjacencies(Point2D point) {
        HashSet<Vector2D> points = new HashSet<>();
        points.add(new Vector2D(-1, -1));
        points.add(new Vector2D(-1, 0));
        points.add(new Vector2D(-1, 1));
        points.add(new Vector2D(0, -1));
        points.add(new Vector2D(0, 1));
        points.add(new Vector2D(1, -1));
        points.add(new Vector2D(1, 0));
        points.add(new Vector2D(1, 1));
        return points;
    }
}
