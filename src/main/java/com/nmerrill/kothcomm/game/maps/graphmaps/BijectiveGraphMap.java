package com.nmerrill.kothcomm.game.maps.graphmaps;

import com.nmerrill.kothcomm.utils.bijectives.Bijective;
import com.nmerrill.kothcomm.game.maps.BijectiveGameMap;
import com.nmerrill.kothcomm.game.maps.MapPoint;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.UnsortedSetIterable;


public class BijectiveGraphMap<U extends MapPoint, T, V extends MapPoint, W>
        extends BijectiveGameMap<U, T, V, W>
        implements GraphMap<U, T>{
    private final GraphMap<V, W> map;
    public BijectiveGraphMap(GraphMap<V, W> map, Bijective<U, V> key, Bijective<T, W> value){
        super(map, key, value);
        this.map = map;
    }

    @Override
    public boolean isEmpty(U point) {
        return map.isEmpty(key.to(point));
    }

    @Override
    public UnsortedSetIterable<U> getNeighbors(U origin) {
        return map.getNeighbors(key.to(origin)).collect(key::from);
    }

    @Override
    public MutableList<T> items() {
        return map.items().collect(value::from);
    }

    @Override
    public boolean isNeighbor(U origin, U neighbor) {
        return map.isNeighbor(key.to(origin), key.to(neighbor));
    }

    @Override
    public MutableSet<U> locations() {
        return map.locations().collect(key::from);
    }
}
