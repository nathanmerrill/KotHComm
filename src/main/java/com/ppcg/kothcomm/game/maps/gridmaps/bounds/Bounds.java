package com.ppcg.kothcomm.game.maps.gridmaps.bounds;

import com.ppcg.kothcomm.game.maps.MapPoint;

public interface Bounds<T extends MapPoint> {
    boolean outOfBounds(T point);
    default boolean inBounds(T point){
        return !outOfBounds(point);
    }
    T wrap(T point);
}
