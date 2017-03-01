package com.nmerrill.kothcomm.game.maps.graphmaps;


import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.game.maps.ReadonlyGameMap;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Region;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.UnsortedSetIterable;

public class ReadonlyGraphMap<U extends MapPoint, T> extends ReadonlyGameMap<U, T>{
    private final GraphMap<U, T> map;
    public ReadonlyGraphMap(GraphMap<U, T> map){
        super(map);
        this.map = map;
    }

    public UnsortedSetIterable<U> getNeighbors(U origin){
        return map.getNeighbors(origin);
    }

    public UnsortedSetIterable<U> getNeighbors(U origin, int maxDistance){
        return map.getNeighbors(origin, maxDistance);
    }

    public boolean isNeighbor(U origin, U neighbor){
        return map.isNeighbor(origin, neighbor);
    }

    public boolean isEmpty(U point){
        return map.isEmpty(point);
    }

    public boolean isFilled(U point) {
        return map.isFilled(point);
    }

    public MutableSet<U> locations(){
        return map.locations();
    }

    public U randomLocation(){
        return map.randomLocation();
    }

    public U randomEmptyLocation(){
        return map.randomEmptyLocation();
    }

    public U randomFilledLocation(){
        return map.randomFilledLocation();
    }

    public ReadonlyGraphMap<U, T> subMap(Region<U> region){
        return new ReadonlyGraphMap<>(map.subMap(region));
    }

    public MutableList<T> items(){
        return map.items();
    }

    public MutableSet<U> emptyLocations() {
        return map.emptyLocations();
    }

    public MutableSet<U> filledLocations(){
        return map.filledLocations();
    }

    public boolean isPath(U from, U to){
        return map.isPath(from, to);
    }

    public MutableList<U> shortestPath(U from, U to){
        return map.shortestPath(from, to);
    }

}
