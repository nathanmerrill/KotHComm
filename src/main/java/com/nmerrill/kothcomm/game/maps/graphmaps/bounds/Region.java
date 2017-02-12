package com.nmerrill.kothcomm.game.maps.graphmaps.bounds;

import com.nmerrill.kothcomm.exceptions.PointOutOfBoundsException;
import com.nmerrill.kothcomm.game.maps.MapPoint;

public interface Region<T extends MapPoint> {
    boolean outOfBounds(T point);
    T startingPoint();
    default boolean inBounds(T point){
        return !outOfBounds(point);
    }
    default void checkBounds(T point){
        if (outOfBounds(point)){
            throw new PointOutOfBoundsException();
        }
    }
}
