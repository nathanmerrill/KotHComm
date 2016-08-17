package com.ppcg.kothcomm.game.maps.gridmaps.bounds;

import com.ppcg.kothcomm.game.maps.MapPoint;

public interface Bounds<T extends MapPoint> {
    boolean outOfBounds(T point);
    T wrap(T point);
}
