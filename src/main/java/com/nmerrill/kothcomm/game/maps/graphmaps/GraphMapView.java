package com.nmerrill.kothcomm.game.maps.graphmaps;


import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Region;
import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.utils.iterables.Itertools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;

import java.util.Iterator;
import java.util.Random;

public class GraphMapView<U extends MapPoint, T> implements GraphMap<U, T>{

    private final Region<U> region;
    private final GraphMap<U, T> map;

    public GraphMapView(GraphMap<U, T> map, Region<U> region){
        this.region = region;
        this.map = map;
    }

    public MutableSet<U> getNeighbors(U origin){
        region.checkBounds(origin);
        return map.getNeighbors(origin).select(region::inBounds);
    }

    public MutableSet<U> getNeighbors(U origin, int maxDistance){
        region.checkBounds(origin);
        return map.getNeighbors(origin, maxDistance).select(region::inBounds);
    }

    public boolean isNeighbor(U origin, U neighbor){
        region.checkBounds(origin);
        region.checkBounds(neighbor);
        return map.isNeighbor(origin, neighbor);
    }

    public boolean isEmpty(U point){
        region.checkBounds(point);
        return map.isEmpty(point);
    }

    public boolean isFilled(U point) {
        region.checkBounds(point);
        return map.isFilled(point);
    }

    public MutableSet<U> locations(){
        return map.locations().select(region::inBounds);
    }

    public U randomLocation(Random random){
        return Itertools.sample(locations().toList(), random);
    }

    public U randomEmptyLocation(Random random){
        return Itertools.sample(emptyLocations().toList(), random);
    }

    public U randomFilledLocation(Random random){
        return Itertools.sample(emptyLocations().toList(), random);
    }

    @Override
    public T get(U point) {
        if (region.outOfBounds(point)){
            return null;
        }
        return map.get(point);
    }

    @Override
    public boolean outOfBounds(U point) {
        return region.outOfBounds(point) || map.outOfBounds(point);
    }

    @Override
    public boolean inBounds(U point) {
        return region.inBounds(point) && map.inBounds(point);
    }

    @Override
    public boolean contains(U point) {
        return region.inBounds(point) && map.contains(point);
    }

    @Override
    public boolean contains(U point, T item) {
        return region.inBounds(point) && map.contains(point, item);
    }

    @Override
    public T clear(U point) {
        region.checkBounds(point);
        return map.clear(point);
    }

    @Override
    public MutableMap<U, T> toMap() {
        return this.map.toMap().keyValuesView()
                .reject(p -> region.outOfBounds(p.getOne()))
                .toMap(Pair::getOne, Pair::getTwo);
    }

    @Override
    public GraphMapView<U, T> subMap(Region<U> region) {
        return new GraphMapView<>(this, region);
    }

    public MutableList<T> items(){
        return this.toMap().valuesView().toList();
    }

    public MutableSet<U> emptyLocations() {
        return map.emptyLocations().select(region::inBounds);
    }

    public MutableSet<U> filledLocations(){
        return map.filledLocations().select(region::inBounds);
    }

    public boolean isPath(U from, U to){
        region.checkBounds(from);
        region.checkBounds(to);
        return map.isPath(from, to);
    }

    @Override
    public void put(U point, T item) {
        region.checkBounds(point);
        map.put(point, item);
    }

    @Override
    public Iterator<U> iterator() {
        return locations().iterator();
    }

    public MutableList<U> shortestPath(U from, U to){
        if (region.outOfBounds(from) || region.outOfBounds(to)){
            return Lists.mutable.empty();
        }
        return map.shortestPath(from, to);
    }

}
