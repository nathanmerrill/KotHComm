package game.maps.gridmaps;

import game.maps.gridmaps.adjacencies.HexagonalAdjacencySet;

public class HexagonGrid<T> extends ShapedGridMap<T, HexagonalAdjacencySet>{
    private final int sideLength;
    public HexagonGrid(int sideLength){
        super(sideLength*2-1, new HexagonalAdjacencySet());
        this.sideLength = sideLength;
    }

    @Override
    public int minX(int y) {
        if (y < sideLength){
            return 0;
        }
        return y - sideLength + 1;
    }

    @Override
    public int maxX(int y) {
        if (y < sideLength) {
            return sideLength + y;
        }
        return sideLength*2-1;
    }
}
