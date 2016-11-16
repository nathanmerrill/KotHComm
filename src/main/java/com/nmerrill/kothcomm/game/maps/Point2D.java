package com.nmerrill.kothcomm.game.maps;

import com.nmerrill.kothcomm.utils.MathTools;

@SuppressWarnings("unused")
public final class Point2D extends MapPoint implements Comparable<Point2D> {
    private final int x, y;
    public Point2D(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point2D that = (Point2D) o;

        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public int compareTo(Point2D o) {
        if (y == o.y){
            return x - o.x;
        } else {
            return y - o.y;
        }
    }

    public int cartesianDistance(Point2D other){
        return Math.abs(other.x-x)+Math.abs(other.y-y);
    }

    public int mooreDistance(Point2D other){
        return Math.max(Math.abs(other.x-x), Math.abs(other.y-y));
    }

    public double diagonalDistance(Point2D other){
        return Math.sqrt(Math.pow(other.x-x, 2)+Math.pow(other.y-y, 2));
    }

    public Point2D move(int x, int y){
        return new Point2D(this.x + x, this.y + y);
    }

    public Point2D moveX(int x){
        return move(x, 0);
    }

    public Point2D moveY(int y){
        return move(0, y);
    }

    public Point2D wrapY(int maxY){
        return new Point2D(x, MathTools.modulo(y, maxY));
    }

    public Point2D wrapX(int maxX){
        return new Point2D(MathTools.modulo(x, maxX), y);
    }

}
