package com.nmerrill.kothcomm.game.maps.graphmaps.bounds.point2D;

import com.nmerrill.kothcomm.game.maps.Point2D;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.WrappingRegion;
import com.nmerrill.kothcomm.utils.MathTools;

public final class SquareRegion implements WrappingRegion<Point2D> {
    private final int left, right, top, bottom, width, height;

    public SquareRegion(Point2D origin, Point2D opposite){
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
        width = right - left;
        height = top - bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public SquareRegion(int size){
        this(new Point2D(0,0), new Point2D(size-1, size-1));
    }

    @Override
    public Point2D wrap(Point2D point) {
        return new Point2D(
                wrap(point.getX(), left, width),
                wrap(point.getY(), bottom, height)
        );
    }

    private int wrap(int val, int min, int range){
        return MathTools.modulo(val - min, range) + min;
    }

    @Override
    public Point2D startingPoint() {
        return new Point2D(left, top);
    }

    @Override
    public boolean outOfBounds(Point2D p) {
        int x = p.getX();
        int y = p.getY();
        return x < left || x > right || y > top || y < bottom;
    }
}
