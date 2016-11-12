package com.nmerrill.kothcomm.game.maps.graphmaps.adjacencies;

import com.nmerrill.kothcomm.game.maps.Point2D;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

public final class MooreAdjacencySet implements AdjacencySet<Point2D> {
    @Override
    public MutableSet<Point2D> getAdjacencies(Point2D point) {
        return Sets.mutable.of(
                point.move(-1, -1),
                point.move(-1, 0),
                point.move(-1, 1),
                point.move(0, -1),
                point.move(0, 1),
                point.move(1, -1),
                point.move(1, 0),
                point.move(1, 1)
        );
    }
}
