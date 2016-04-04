package game.maps;

import java.util.*;

public interface ReadonlyGameMap<T, U extends MapPoint>  {
    T get(U point);

    U locationOf(T item);

    boolean isNeighbor(U origin, U destination);

    boolean itemAt(U point);

    boolean itemAt(U point, T item);

    boolean contains(T item);

    HashMap<T, U> items();

    HashMap<U, T> filledLocations();

    List<U> getNeighbors(U point);

    boolean isConnected(U from, U to);

    List<U> findPath(U from, U to);
}
