package com.ppcg.kothcomm.game.maps;

import org.eclipse.collections.api.map.ImmutableMap;


public interface ReadonlyGameMap<U extends MapPoint, T>  {
    T get(U point);

    boolean outOfBounds(U point);

    boolean contains(U point, T item);

    boolean contains(U point);

    ImmutableMap<U, T> toMap();
}
