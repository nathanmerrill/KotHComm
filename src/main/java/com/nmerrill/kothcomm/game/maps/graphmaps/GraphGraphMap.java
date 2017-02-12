package com.nmerrill.kothcomm.game.maps.graphmaps;

import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Region;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Multimaps;

import java.util.Objects;


public class GraphGraphMap<U extends MapPoint, T> implements GraphMap<U, T>{

    private final MutableMultimap<U, U> connections;
    private final MutableMap<U, T> items;

    public GraphGraphMap(){
        connections = Multimaps.mutable.set.empty();
        items = Maps.mutable.empty();
    }

    @Override
    public GraphGraphMap<U, T> subMap(Region<U> region) {
        GraphGraphMap<U, T> map = new GraphGraphMap<>();
        map.connections.putAll(connections.selectKeysValues((i, j) -> region.inBounds(i) && region.inBounds(j)));
        map.items.putAll(items.select((i, j) -> region.inBounds(i)));
        return map;
    }

    @Override
    public boolean outOfBounds(U point) {
        return items.containsKey(point);
    }

    @Override
    public MutableSet<U> getNeighbors(U origin) {
        return connections.get(origin).toSet();
    }

    @Override
    public T get(U point) {
        return items.get(point);
    }

    @Override
    public T clear(U point) {
        T item = items.remove(point);
        if (item != null) {
            connections.removeAll(point)
                    .forEach(connection -> connections.get(connection).remove(point));
        }
        return item;
    }

    public void connect(U point, U neighbor){
        connections.get(point).add(neighbor);
        connections.get(neighbor).add(point);
    }

    public void disconnect(U point, U neighbor){
        connections.get(point).remove(neighbor);
        connections.get(neighbor).remove(point);
    }

    @Override
    public void put(U point, T item) {
        items.put(point, item);
    }

    @Override
    public boolean contains(U point) {
        return items.containsKey(point);
    }


    @Override
    public MutableMap<U, T> toMap() {
        return null;
    }

    @Override
    public boolean contains(U point, T item) {
        if (contains(point)){
            return false;
        }
        return Objects.equals(items.get(point), item);
    }

    @Override
    public MutableSet<U> locations() {
        return items.keysView().toSet();
    }

    @Override
    public boolean isFilled(U point) {
        return items.get(point) != null;
    }

    @Override
    public boolean isEmpty(U point) {
        if (!contains(point)){
            return false;
        }
        return items.get(point) == null;
    }

    @Override
    public boolean isNeighbor(U origin, U neighbor) {
        return connections.get(origin).contains(neighbor);
    }

    @Override
    public MutableList<T> items() {
        return items.toList();
    }

}
