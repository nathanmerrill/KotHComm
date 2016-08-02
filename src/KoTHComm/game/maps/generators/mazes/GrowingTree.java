package KoTHComm.game.maps.generators.mazes;

import KoTHComm.game.maps.MapPoint;
import KoTHComm.game.maps.generators.Generator;
import KoTHComm.game.maps.gridmaps.GridMap;
import KoTHComm.utils.Pair;
import KoTHComm.utils.Tools;

import java.util.*;
import java.util.function.ToIntFunction;

public class GrowingTree<U extends MapPoint> implements Generator<GridMap<U, ?>> {

    @SuppressWarnings({"SameReturnValue", "UnusedParameters"})
    public static int selectOldest(List<? extends MapPoint> points){
        return 0;
    }
    public static int selectNewest(List<? extends MapPoint> points){
        return points.size()-1;
    }
    public static int selectRandom(List<? extends MapPoint> points){
        return new Random().nextInt(points.size());
    }
    public static <U extends MapPoint> ToIntFunction<List<U>> randomSelection(Random random){
        return (List<U> list) -> random.nextInt(list.size());
    }

    private final ToIntFunction<List<U>> selectionMethod;
    private final Random random;
    public GrowingTree(ToIntFunction<List<U>> selectionMethod, Random random){
        this.selectionMethod = selectionMethod;
        this.random = random;
    }
    public GrowingTree(ToIntFunction<List<U>> selectionMethod){
        this(selectionMethod, new Random());
    }
    public GrowingTree(Random random){
        this(randomSelection(random), random);
    }
    public GrowingTree(){
        this(new Random());
    }

    @Override
    public void generate(GridMap<U, ?> map) {
        List<U> locations = new ArrayList<>(map.locations());
        U start = Tools.sample(locations, random);
        List<U> borders = new ArrayList<>();
        List<U> visited = new ArrayList<>();
        Set<Pair<U, U>> connections = new HashSet<>();
        visited.add(start);
        borders.addAll(map.getNeighbors(start));
        
        while (!borders.isEmpty()){
            U point = borders.remove(selectionMethod.applyAsInt(borders));
            Set<U> neighbors = map.getNeighbors(point);
            neighbors.removeIf(visited::contains);
            U neighbor = Tools.sample(neighbors, random);
            connections.add(new Pair<>(point, neighbor));
            connections.add(new Pair<>(neighbor, point));
            borders.addAll(neighbors);
        }
        for (U point: map.locations()){
            Set<U> neighbors = map.getNeighbors(point);
            neighbors.removeIf(n -> connections.contains(new Pair<U, U>(point, n)));
            neighbors.forEach(n -> map.disconnect(point, n));
        }
    }

}
