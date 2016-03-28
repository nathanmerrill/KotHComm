package game.maps.graphmaps;

import game.maps.MapPoint;
import utils.iterables.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WeightedGraphMap<T> extends GraphMap<T> {

    private final int defaultCost;
    private final HashMap<Pair<MapPoint, MapPoint>, Integer> costs;
    public WeightedGraphMap(){
        this(1);
    }
    public WeightedGraphMap(int defaultCost){
        this.defaultCost = defaultCost;
        costs = new HashMap<>();
    }

    public void addEdge(MapPoint point1, MapPoint point2, int cost){
        this.addArc(point1, point2, cost);
        this.addArc(point2, point1, cost);
    }

    @Override
    public void addArc(MapPoint origin, MapPoint destination){
        this.addArc(origin, destination, defaultCost);
    }

    public void addArc(MapPoint origin, MapPoint destination, int cost){
        costs.put(new Pair<>(origin, destination), cost);
        super.addArc(origin, destination);
    }

    @Override
    public void removeArc(MapPoint origin, MapPoint destination){
        costs.remove(new Pair<>(origin, destination));
        super.removeArc(origin, destination);
    }

    public int getCost(MapPoint origin, MapPoint destination){
        return costs.get(new Pair<>(origin, destination));
    }

    public Map<MapPoint, Integer> getNeighborCosts(MapPoint point){
        return super.getNeighbors(point).stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        e -> getCost(point, e)));
    }
}
