package com.nmerrill.kothcomm.game.maps.graphmaps.bounds;

import com.nmerrill.kothcomm.game.maps.MapPoint;

public interface WrappingBounds<T extends MapPoint> extends Bounds<T> {
    T wrap(T point);
}
