package game.maps.gridmaps;

import game.maps.MapPoint;
import utils.Tools;

public class Point2D extends MapPoint implements Comparable<Point2D> {
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

    public Point2D wrapY(int maxY){
        return new Point2D(x, Tools.modulo(y, maxY));
    }

    public Point2D wrapX(int maxX){
        return new Point2D(Tools.modulo(x, maxX), y);
    }

}
