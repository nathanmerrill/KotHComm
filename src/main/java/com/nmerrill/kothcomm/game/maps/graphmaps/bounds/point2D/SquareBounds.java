package com.nmerrill.kothcomm.game.maps.graphmaps.bounds.point2D;

import com.nmerrill.kothcomm.game.maps.Point2D;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Bounds;

public final class SquareBounds implements Bounds<Point2D> {
    private final int left, right, top, bottom;

    public SquareBounds(Point2D origin, Point2D opposite){
        if (origin.getX() == opposite.getX() || origin.getY() == opposite.getY()){
            throw new IllegalArgumentException("Origin and opposite cannot have the same x or y coordinates");
        }
        if (origin.getX() < opposite.getX()){
            left = origin.getX();
            right = opposite.getX();
        } else {
            left = opposite.getX();
            right = origin.getX();
        }
        if (origin.getY() < opposite.getY()){
            bottom = origin.getY();
            top = opposite.getY();
        } else {
            bottom = opposite.getY();
            top = origin.getY();
        }
    }

    public SquareBounds(int size){
        this(new Point2D(0,0), new Point2D(size-1, size-1));
    }

    @Override
    public boolean outOfBounds(Point2D p) {
        int x = p.getX();
        int y = p.getY();
        return x < left || x > right || y > top || y < bottom;
    }
}
