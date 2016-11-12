package com.nmerrill.kothcomm.game.maps;

import com.nmerrill.kothcomm.utils.bijectives.Bijective;
import org.eclipse.collections.api.map.MutableMap;


public class BijectiveGameMap<U extends MapPoint, T, V extends MapPoint, W> implements GameMap<U, T> {
    protected final Bijective<U, V> key;
    protected final Bijective<T, W> value;
    private final GameMap<V, W> map;
    public BijectiveGameMap(GameMap<V, W> map, Bijective<U, V> key, Bijective<T, W> value){
        this.map = map;
        this.key = key;
        this.value = value;
    }

    public T get(U point){
        return value.from(map.get(key.to(point)));
    }

    public boolean contains(U point, T item){
        return map.contains(key.to(point), value.to(item));
    }

    public boolean contains(U point){
        return map.contains(key.to(point));
    }

    public boolean outOfBounds(U point){
        return map.outOfBounds(key.to(point));
    }

    public MutableMap<U, T> toMap(){
        return map.toMap().keyValuesView().toMap(p -> key.from(p.getOne()), p -> value.from(p.getTwo()));
    }

    public boolean inBounds(U point){
        return map.inBounds(key.to(point));
    }

    @Override
    public T clear(U point) {
        return value.from(map.clear(key.to(point)));
    }

    @Override
    public void put(U point, T item) {
        map.put(key.to(point), value.to(item));
    }

    public T move(U location, U destination){
        return value.from(map.move(key.to(location), key.to(destination)));
    }
}
