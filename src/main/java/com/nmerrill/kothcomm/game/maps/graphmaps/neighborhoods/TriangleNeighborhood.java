package com.nmerrill.kothcomm.game.maps.graphmaps.neighborhoods;

import com.nmerrill.kothcomm.game.maps.Point2D;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;


public final class TriangleNeighborhood implements Neighborhood<Point2D> {
    @Override
    public MutableSet<Point2D> getAdjacencies(Point2D point) {
        boolean pointUp = (point.getX()+point.getY())%2 == 0;
        return Sets.mutable.of(
                point.move(-1, 0),
                point.move(0, pointUp?-1:1),
                point.move(1, 0)
        );
    }
}
