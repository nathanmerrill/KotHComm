package game.maps.gridmaps;

import game.maps.gridmaps.adjacencies.HexagonalAdjacencySet;

public class TriangularHexagonMap<T> extends ShapedGridMap<T, HexagonalAdjacencySet> {

    private final int sideLength;
    public TriangularHexagonMap(int sideLength){
        super(sideLength, new HexagonalAdjacencySet());
        this.sideLength = sideLength;
    }
    @Override
    public int minX(int y) {
        return y;
    }

    @Override
    public int maxX(int y) {
        return sideLength;
    }
}
