package com.nmerrill.kothcomm.game.maps.graphmaps;

import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Region;
import com.nmerrill.kothcomm.game.maps.graphmaps.neighborhoods.Neighborhood;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Maps;

import java.util.Iterator;
import java.util.Random;

public class NeighborhoodGraphMap<U extends MapPoint, T> implements GraphMap<U, T>{
    private final Region<U> region;
    private final Neighborhood<U> neighborhood;
    private final MutableMap<U, T> points;

    public NeighborhoodGraphMap(Region<U> region, Neighborhood<U> neighborhood){
        this.region = region;
        this.neighborhood = neighborhood;
        this.points = Maps.mutable.empty();
    }

    @Override
    public boolean isEmpty(U point) {
        region.checkBounds(point);
        return !points.containsKey(point);
    }

    @Override
    public ImmutableSet<U> getNeighbors(U origin) {
        region.checkBounds(origin);
        return neighborhood.getAdjacencies(origin).select(region::inBounds);
    }

    @Override
    public MutableList<T> items() {
        return points.valuesView().toList();
    }

    @Override
    public boolean isNeighbor(U origin, U neighbor) {
        region.checkBounds(origin);
        region.checkBounds(neighbor);
        return neighborhood.getAdjacencies(origin).contains(neighbor);
    }

    @Override
    public void put(U point, T item) {
        region.checkBounds(point);
        if (item == null){
            points.remove(point);
        } else {
            points.put(point, item);
        }
    }

    @Override
    public T clear(U point) {
        region.checkBounds(point);
        return points.remove(point);
    }

    @Override
    public T get(U point) {
        region.checkBounds(point);
        return points.get(point);
    }

    @Override
    public Iterator<U> iterator() {
        return points.keysView().iterator();
    }

    @Override
    public MutableMap<U, T> toMap() {
        return points.clone();
    }

    @Override
    public boolean contains(U point) {
        return points.containsKey(point);
    }

    @Override
    public MutableSet<U> locations() {
        return points.keysView().toSet();
    }

    @Override
    public boolean outOfBounds(U point) {
        return region.outOfBounds(point);
    }

    @Override
    public boolean contains(U point, T item) {
        return points.containsKey(point) && points.get(point).equals(item);
    }

    @Override
    public U randomEmptyLocation(Random random) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MutableSet<U> emptyLocations() {
        throw new UnsupportedOperationException();
    }
}
