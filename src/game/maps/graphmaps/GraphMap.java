package game.maps.graphmaps;


import game.maps.GameMap;
import game.maps.MapPoint;

import java.util.*;

public class GraphMap<T> extends GameMap<T, MapPoint> {
    private final HashMap<MapPoint, Set<MapPoint>> connections;
    public GraphMap(){
        connections = new HashMap<>();
    }

    public void addEdge(MapPoint point1, MapPoint point2){
        addArc(point1, point2);
        addArc(point2, point1);
    }

    public void addArc(MapPoint origin, MapPoint destination){
        connections.putIfAbsent(origin, new HashSet<>());
        connections.get(origin).add(destination);
    }

    public void removeEdge(MapPoint point1, MapPoint point2){
        removeArc(point1, point2);
        removeArc(point2, point1);
    }

    public void removeArc(MapPoint origin, MapPoint destination){
        if (connections.containsKey(origin)){
            connections.get(origin).remove(destination);
        }
    }

    @Override
    public List<MapPoint> getNeighbors(MapPoint point) {
        return new ArrayList<>(connections.get(point));
    }
}
