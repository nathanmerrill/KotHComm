package game.maps;

import java.util.List;

public interface ReadonlyBoundedMap<T, U extends MapPoint> extends ReadonlyGameMap<T, U>{
    boolean inBounds(U point);

    Iterable<U> allLocationsIter();

    List<U> allLocations();

    List<U> emptyLocations();
}
