package com.nmerrill.kothcomm.game.maps.graphmaps;

import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Bounds;
import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.game.maps.graphmaps.adjacencies.AdjacencySet;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Maps;

import java.util.Iterator;
import java.util.Random;

public class AdjacencyGraphMap<U extends MapPoint, T> implements GraphMap<U, T>{
    private final Bounds<U> bounds;
    private final AdjacencySet<U> adjacencySet;
    private final MutableMap<U, T> points;

    public AdjacencyGraphMap(Bounds<U> bounds, AdjacencySet<U> adjacencySet){
        this.bounds = bounds;
        this.adjacencySet = adjacencySet;
        this.points = Maps.mutable.empty();
    }

    @Override
    public boolean isEmpty(U point) {
        bounds.checkBounds(point);
        return !points.containsKey(point);
    }

    @Override
    public MutableSet<U> getNeighbors(U origin) {
        bounds.checkBounds(origin);
        return adjacencySet.getAdjacencies(origin).select(bounds::inBounds);
    }

    @Override
    public MutableList<T> items() {
        return points.valuesView().toList();
    }

    @Override
    public boolean isNeighbor(U origin, U neighbor) {
        bounds.checkBounds(origin);
        bounds.checkBounds(neighbor);
        return adjacencySet.getAdjacencies(origin).contains(neighbor);
    }

    @Override
    public void put(U point, T item) {
        bounds.checkBounds(point);
        if (item == null){
            points.remove(point);
        } else {
            points.put(point, item);
        }
    }

    @Override
    public T clear(U point) {
        bounds.checkBounds(point);
        return points.remove(point);
    }

    @Override
    public T get(U point) {
        bounds.checkBounds(point);
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
        return bounds.outOfBounds(point);
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
