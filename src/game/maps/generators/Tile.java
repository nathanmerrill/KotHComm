package game.maps.generators;

import game.maps.MapPoint;
import game.maps.gridmaps.GridMap;
import game.maps.gridmaps.adjacencies.AdjacencySet;
import game.maps.gridmaps.bounds.Bounds;

import java.util.stream.Collectors;


public class Tile<U extends MapPoint> implements Generator<GridMap<U, ?>>{
    private final U startingPoint;
    private final Bounds<U> bounds;
    private final AdjacencySet<U> adjacencySet;
    public Tile(U startingPoint, Bounds<U> bounds, AdjacencySet<U> adjacencySet){
        this.startingPoint = startingPoint;
        this.bounds = bounds;
        this.adjacencySet = adjacencySet;
    }

    @Override
    public void generate(GridMap<U, ?> map) {
        generate(map, startingPoint);
    }

    private void generate(GridMap<U, ?> map, U point){
        map.put(point, null);
        adjacencySet.getAdjacencies(point).stream()
                .map(v -> v.move(point))
                .map(bounds::wrap)
                .filter(p -> !bounds.outOfBounds(p))
                .filter(p -> !map.contains(p))
                .collect(Collectors.toList())
                .forEach(n -> {
                    map.connect(point, n);
                    generate(map, n);
                });
    }
}
