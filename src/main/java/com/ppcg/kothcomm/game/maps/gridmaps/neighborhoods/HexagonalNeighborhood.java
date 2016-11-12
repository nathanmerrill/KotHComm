package com.ppcg.kothcomm.game.maps.gridmaps.neighborhoods;

import com.ppcg.kothcomm.game.maps.gridmaps.Point2D;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

public final class HexagonalNeighborhood implements Neighborhood<Point2D> {

    @Override
    public MutableSet<Point2D> getNeighborhood(Point2D point) {
        return Sets.mutable.of(
                point.move(-1, -1),
                point.move(-1, 0),
                point.move(0, -1),
                point.move(0, 1),
                point.move(1, 0),
                point.move(1, 1)
        );
    }
}
