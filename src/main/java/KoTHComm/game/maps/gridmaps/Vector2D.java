package KoTHComm.game.maps.gridmaps;

public class Vector2D extends Vector<Point2D> {
    private final int xOffset, yOffset;
    public Vector2D(int xOffset, int yOffset){
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int xOffset() {
        return xOffset;
    }

    public int yOffset() {
        return yOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector2D that = (Vector2D) o;

        return xOffset == that.xOffset && yOffset == that.yOffset;
    }

    @Override
    public Point2D move(Point2D point) {
        return new Point2D(point.getX()+xOffset, point.getY()+yOffset);
    }

    @Override
    public int hashCode() {
        int result = xOffset;
        result = 31 * result + yOffset;
        return result;
    }
}
