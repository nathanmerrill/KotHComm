package game.maps;

import java.util.*;

public interface ReadonlyGameMap<U extends MapPoint, T>  {
    T get(U point);

    boolean outOfBounds(U point);

    boolean contains(U point, T item);

    boolean contains(U point);

    HashMap<U, T> toMap();
}
