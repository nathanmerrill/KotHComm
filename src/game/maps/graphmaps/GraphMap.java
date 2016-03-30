package game.maps.graphmaps;


import game.maps.GameMap;
import game.maps.MapPoint;

import java.util.*;

/**
 * A map where each point can connect to an unlimited number of points.
 * We consider an Edge to be a two-way connection, i.e., both nodes list each other as a neighbor
 * We consider an Arc to be a one-way connection, i.e., only one node lists the other as a neighbor
 * @param <T> The item stored in the graph
 */
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

    public boolean isEdge(MapPoint point1, MapPoint point2){
        return isNeighbor(point1, point2) && isNeighbor(point2, point1);
    }

    @Override
    public boolean isNeighbor(MapPoint origin, MapPoint destination){
        return connections.get(origin).contains(destination);
    }

    @Override
    public List<MapPoint> getNeighbors(MapPoint point) {
        return new ArrayList<>(connections.get(point));
    }
}
