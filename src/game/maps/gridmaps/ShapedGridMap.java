package game.maps.gridmaps;

import game.maps.BoundedMap;
import game.maps.gridmaps.adjacencies.AdjacencySet;
import utils.iterables.Tools;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ShapedGridMap<T, U extends AdjacencySet>
        extends BoundedMap<T, GridPoint>
        implements ReadonlyShapedGridMap<T> {

    private final int height;
    private final U adjacencies;
    public ShapedGridMap(int height, U adjacencies){
        this.height = height;
        this.adjacencies = adjacencies;
    }

    public U getAdjacencies(){
        return adjacencies;
    }

    private class ShapedMapIterable implements Iterable<GridPoint> {
        private class ShapedMapIterator implements Iterator<GridPoint> {

            private int x, y;

            private ShapedMapIterator() {
                x = 0;
                y = 0;
            }

            @Override
            public GridPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                GridPoint ret = new GridPoint(x, y);
                x++;
                if (x == maxX(y)) {
                    y++;
                    x = minX(y);
                }
                return ret;
            }

            @Override
            public boolean hasNext() {
                return y < height;
            }

        }

        @Override
        public Iterator<GridPoint> iterator() {
            return new ShapedMapIterator();
        }
    }

    @Override
    public Iterable<GridPoint> allLocationsIter() {
        return new ShapedMapIterable();
    }


    @Override
    public List<GridPoint> getNeighbors(GridPoint point) {
        return adjacencies.getAdjacencies(point).stream()
                .map(point::move)
                .filter(this::inBounds)
                .collect(Collectors.toList());

    }

    @Override
    public boolean inBounds(GridPoint point) {
        int y = point.getY();
        return Tools.inBounds(y, 0, height) &&
                Tools.inBounds(point.getX(), minX(y), maxX(y));
    }

    @Override
    protected GridPoint wrapPoint(GridPoint point) {
        return point;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxWidth(){
        return IntStream.range(0, height).map(this::maxX).max().getAsInt();
    }

    public abstract int minX(int y);
    public abstract int maxX(int y);
}
