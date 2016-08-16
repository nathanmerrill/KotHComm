package KoTHComm.game.maps.gridmaps.bounds;

import KoTHComm.game.maps.MapPoint;

public interface Bounds<T extends MapPoint> {
    boolean outOfBounds(T point);
    T wrap(T point);
}
