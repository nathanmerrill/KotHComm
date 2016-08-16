package KoTHComm.game.maps.gridmaps;

import KoTHComm.game.maps.MapPoint;

public abstract class Vector<T extends MapPoint> {
    public abstract T move(T point);
}
