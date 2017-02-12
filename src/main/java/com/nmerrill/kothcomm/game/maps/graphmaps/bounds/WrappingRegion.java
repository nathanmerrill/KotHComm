package com.nmerrill.kothcomm.game.maps.graphmaps.bounds;

import com.nmerrill.kothcomm.game.maps.MapPoint;

public interface WrappingRegion<T extends MapPoint> extends Region<T> {
    T wrap(T point);
}
