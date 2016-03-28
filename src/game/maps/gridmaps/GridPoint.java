package game.maps.gridmaps;

import game.maps.MapPoint;
import org.jetbrains.annotations.NotNull;
import utils.iterables.Tools;


public final class GridPoint extends MapPoint implements Comparable<GridPoint>{
    private final int x, y;
    public GridPoint(int x, int y){
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

        GridPoint that = (GridPoint) o;

        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public int compareTo(@NotNull GridPoint o) {
        if (y == o.y){
            return x - o.x;
        } else {
            return y - o.y;
        }
    }

    public GridPoint wrapY(int maxY){
        return new GridPoint(x, Tools.modulo(y, maxY));
    }

    public GridPoint wrapX(int maxX){
        return new GridPoint(Tools.modulo(x, maxX), y);
    }

    public GridPoint move(GridVector vector){
        return new GridPoint(x+vector.xOffset(), y+vector.yOffset());
    }
}
