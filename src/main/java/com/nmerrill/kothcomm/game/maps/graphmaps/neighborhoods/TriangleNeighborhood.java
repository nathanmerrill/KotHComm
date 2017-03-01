package com.nmerrill.kothcomm.game.maps.graphmaps.neighborhoods;

import com.nmerrill.kothcomm.game.maps.Point2D;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.impl.factory.Sets;


public final class TriangleNeighborhood implements Neighborhood<Point2D> {
    @Override
    public ImmutableSet<Point2D> getAdjacencies(Point2D point) {
        boolean pointUp = (point.getX()+point.getY())%2 == 0;
        return Sets.immutable.of(
                point.move(-1, 0),
                point.move(0, pointUp?-1:1),
                point.move(1, 0)
        );
    }
}
