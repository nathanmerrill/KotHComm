package game.maps.gridmaps.bounds;

import game.maps.MapPoint;

public interface Bounds<T extends MapPoint> {
    boolean outOfBounds(T point);
    T wrap(T point);
}
