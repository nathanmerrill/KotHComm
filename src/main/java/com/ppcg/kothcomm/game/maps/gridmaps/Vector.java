package com.ppcg.kothcomm.game.maps.gridmaps;

import com.ppcg.kothcomm.game.maps.MapPoint;

public abstract class Vector<T extends MapPoint> {
    public abstract T move(T point);
}
