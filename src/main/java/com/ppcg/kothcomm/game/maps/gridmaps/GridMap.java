package com.ppcg.kothcomm.game.maps.gridmaps;


import com.ppcg.kothcomm.game.maps.GameMap;
import com.ppcg.kothcomm.game.maps.MapPoint;
import com.ppcg.kothcomm.game.maps.gridmaps.bounds.Bounds;
import com.ppcg.kothcomm.utils.Tools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Multimaps;

import java.util.Iterator;
import java.util.Objects;


/**
 * A map with a finite number of locations and connections between those locations
 * @param <U>
 * @param <T>
 */
public class GridMap<U extends MapPoint, T>
        implements GameMap<U, T>, ReadonlyGridMap<U, T> {

    private final MutableMultimap<U, U> connections;
    private final MutableMap<U, T> items;

    public GridMap(){
        connections = Multimaps.mutable.set.empty();
        items = Maps.mutable.empty();
    }

    @Override
    public U randomPoint(boolean canBeEmpty) {
        return Tools.sample(connections.keyBag().toList());
    }

    @Override
    public ReadonlyGridMap<U, T> subMap(Bounds<U> bounds) {
        GridMap<U, T> map = new GridMap<>();
        map.connections.putAll(connections.selectKeysValues((i, j) -> bounds.inBounds(i) && bounds.inBounds(j)));
        map.items.putAll(items.select((i, j) -> bounds.inBounds(i)));
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
    public Iterator<U> iterator() {
        return locations().iterator();
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
    public ImmutableMap<U, T> toMap() {
        return items.toImmutable();
    }

    @Override
    public boolean contains(U point, T item) {
        //noinspection SimplifiableIfStatement
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
        //noinspection SimplifiableIfStatement
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
