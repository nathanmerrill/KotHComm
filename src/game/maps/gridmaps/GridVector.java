package game.maps.gridmaps;

public class GridVector {
    private final int xOffset, yOffset;
    public GridVector(int xOffset, int yOffset){
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

        GridVector that = (GridVector) o;

        return xOffset == that.xOffset && yOffset == that.yOffset;

    }

    @Override
    public int hashCode() {
        int result = xOffset;
        result = 31 * result + yOffset;
        return result;
    }
}
