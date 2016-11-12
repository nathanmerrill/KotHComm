package com.nmerrill.kothcomm.game.maps.graphmaps.bounds;

import com.nmerrill.kothcomm.exceptions.PointOutOfBoundsException;
import com.nmerrill.kothcomm.game.maps.MapPoint;

public interface Bounds<T extends MapPoint> {
    boolean outOfBounds(T point);
    default boolean inBounds(T point){
        return !outOfBounds(point);
    }
    default void checkBounds(T point){
        if (outOfBounds(point)){
            throw new PointOutOfBoundsException();
        }
    }
}
