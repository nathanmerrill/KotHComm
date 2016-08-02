package KoTHComm.game.maps;

/**
 * A AbstractGame map
 * A location can only contain 1 item
 * @param <T>
 * @param <U>
 */
public interface GameMap<U extends MapPoint, T> extends ReadonlyGameMap<U, T>{
    void put(U point, T item);

    T clear(U point);

    default T move(U location, U destination){
        T item = clear(location);
        put(destination, item);
        return item;
    }

}
