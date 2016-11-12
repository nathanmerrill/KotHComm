package com.nmerrill.kothcomm.game.maps;

import org.eclipse.collections.api.map.MutableMap;

/**
 * A AbstractGame map
 * A location can only contain 1 item
 * @param <T>
 * @param <U>
 */
public interface GameMap<U extends MapPoint, T>{
    void put(U point, T item);

    T clear(U point);

    T get(U point);

    boolean contains(U point, T item);

    boolean contains(U point);

    boolean outOfBounds(U point);

    MutableMap<U, T> toMap();

    default ReadonlyGameMap<U, T> readonly(){
        return new ReadonlyGameMap<>(this);
    }

    default boolean inBounds(U point){
        return !outOfBounds(point);
    }

    default T move(U location, U destination){
        T item = clear(location);
        put(destination, item);
        return item;
    }

}
