package com.nmerrill.kothcomm.game.maps.graphmaps;


import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Bounds;
import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.utils.iterables.Itertools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;

import java.util.Iterator;
import java.util.Random;

public class BoundedGraphMap<U extends MapPoint, T> implements GraphMap<U, T>{

    private final Bounds<U> bounds;
    private final GraphMap<U, T> map;

    public BoundedGraphMap(GraphMap<U, T> map, Bounds<U> bounds){
        this.bounds = bounds;
        this.map = map;
    }

    public MutableSet<U> getNeighbors(U origin){
        bounds.checkBounds(origin);
        return map.getNeighbors(origin).select(bounds::inBounds);
    }

    public MutableSet<U> getNeighbors(U origin, int maxDistance){
        bounds.checkBounds(origin);
        return map.getNeighbors(origin, maxDistance).select(bounds::inBounds);
    }

    public boolean isNeighbor(U origin, U neighbor){
        bounds.checkBounds(origin);
        bounds.checkBounds(neighbor);
        return map.isNeighbor(origin, neighbor);
    }

    public boolean isEmpty(U point){
        bounds.checkBounds(point);
        return map.isEmpty(point);
    }

    public boolean isFilled(U point) {
        bounds.checkBounds(point);
        return map.isFilled(point);
    }

    public MutableSet<U> locations(){
        return map.locations().select(bounds::inBounds);
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
        if (bounds.outOfBounds(point)){
            return null;
        }
        return map.get(point);
    }

    @Override
    public boolean outOfBounds(U point) {
        return bounds.outOfBounds(point) || map.outOfBounds(point);
    }

    @Override
    public boolean inBounds(U point) {
        return bounds.inBounds(point) && map.inBounds(point);
    }

    @Override
    public boolean contains(U point) {
        return bounds.inBounds(point) && map.contains(point);
    }

    @Override
    public boolean contains(U point, T item) {
        return bounds.inBounds(point) && map.contains(point, item);
    }

    @Override
    public T clear(U point) {
        bounds.checkBounds(point);
        return map.clear(point);
    }

    @Override
    public MutableMap<U, T> toMap() {
        return this.map.toMap().keyValuesView()
                .reject(p -> bounds.outOfBounds(p.getOne()))
                .toMap(Pair::getOne, Pair::getTwo);
    }

    @Override
    public BoundedGraphMap<U, T> subMap(Bounds<U> bounds) {
        return new BoundedGraphMap<>(this, bounds);
    }

    public MutableList<T> items(){
        return this.toMap().valuesView().toList();
    }

    public MutableSet<U> emptyLocations() {
        return map.emptyLocations().select(bounds::inBounds);
    }

    public MutableSet<U> filledLocations(){
        return map.filledLocations().select(bounds::inBounds);
    }

    public boolean isPath(U from, U to){
        bounds.checkBounds(from);
        bounds.checkBounds(to);
        return map.isPath(from, to);
    }

    @Override
    public void put(U point, T item) {
        bounds.checkBounds(point);
        map.put(point, item);
    }

    @Override
    public Iterator<U> iterator() {
        return locations().iterator();
    }

    public MutableList<U> shortestPath(U from, U to){
        if (bounds.outOfBounds(from) || bounds.outOfBounds(to)){
            return Lists.mutable.empty();
        }
        return map.shortestPath(from, to);
    }

}
