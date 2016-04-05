package game.maps.gridmaps;

import game.maps.BoundedMap;
import game.maps.gridmaps.adjacencies.AdjacencySet;
import game.maps.gridmaps.adjacencies.SquareAdjacencySet;
import utils.iterables.Tools;

import java.util.List;
import java.util.stream.Collectors;

public class TilingMap<T> extends BoundedMap<T, GridPoint> {


    private final int width, height;
    private final boolean wrapX, wrapY;
    private final AdjacencySet adjacencies;

    public TilingMap(int width, int height, boolean wrapX, boolean wrapY, AdjacencySet adjacencies){
        this.width = width*2-1;
        this.height = height;
        this.wrapX = wrapX;
        this.wrapY = wrapY;
        this.adjacencies = adjacencies;
    }

    public TilingMap(int width, int height, boolean wrapX, boolean wrapY){
        this(width, height, wrapX, wrapY, new SquareAdjacencySet());
    }

    public TilingMap(int width, int height, boolean wraps, AdjacencySet adjacencies){
        this(width, height, wraps, wraps, adjacencies);
    }

    public TilingMap(int width, int height, AdjacencySet adjacencies){
        this(width, height, false, adjacencies);
    }

    public TilingMap(int width, int height, boolean wraps){
        this(width, height, wraps, wraps);
    }

    public TilingMap(int width, int height){
        this(width, height, false);
    }

    public TilingMap(int size, boolean wraps, AdjacencySet adjacencies){
        this(size, size, wraps, wraps, adjacencies);
    }

    public TilingMap(int size, AdjacencySet adjacencies){
        this(size, false, adjacencies);
    }

    public TilingMap(int size, boolean wraps){
        this(size, wraps, new SquareAdjacencySet());
    }

    public TilingMap(int size){
        this(size, false);
    }


    @Override
    protected GridPoint wrapPoint(GridPoint point){
        if (wrapX){
            point = point.wrapX(width);
        }
        if (wrapY){
            point = point.wrapY(height);
        }
        return point;
    }

    @Override
    public Iterable<GridPoint> allLocationsIter() {
        return new GridIterable(width, height);
    }


    @Override
    public boolean inBounds(GridPoint point){
        return (wrapX || Tools.inBounds(point.getX(), 0, width)) &&
                (wrapY || Tools.inBounds(point.getY(), 0, height));
    }

    @Override
    public List<GridPoint> getNeighbors(GridPoint point) {
        return adjacencies.getAdjacencies(point).stream()
                .map(point::move)
                .map(this::wrapPoint)
                .collect(Collectors.toList());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public AdjacencySet getAdjacencies() {
        return adjacencies;
    }
}
