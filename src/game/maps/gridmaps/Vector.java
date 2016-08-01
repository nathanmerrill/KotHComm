package game.maps.gridmaps;

import game.maps.MapPoint;

public abstract class Vector<T extends MapPoint> {
    public abstract T move(T point);
}
