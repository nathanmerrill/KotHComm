package game.maps.gridmaps;


import game.maps.gridmaps.adjacencies.TriangleAdjacencySet;

public class TriangleMap<T> extends ShapedGridMap<T> {

    private final int sideLength;
    public TriangleMap(int sideLength){
        super(sideLength, new TriangleAdjacencySet());
        this.sideLength = sideLength;
    }


    @Override
    public int maxX(int y) {
        return sideLength*2-y-1;
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @Override
    public int minX(int y) {
        return y;
    }

}
