package game.maps;


import java.util.List;

public abstract class BoundedMap<T, U extends MapPoint> extends GameMap<T, U>{

    @Override
    public T get(U point) {
        return super.get(checkBoundsAndWrap(point));
    }

    @Override
    public void put(T item, U point) {
        super.put(item, checkBoundsAndWrap(point));
    }

    @Override
    public T clear(U point) {
        return super.clear(checkBoundsAndWrap(point));
    }

    @Override
    public boolean isConnected(U from, U to) {
        return super.isConnected(checkBoundsAndWrap(from), checkBoundsAndWrap(to));
    }

    @Override
    public boolean isNeighbor(U origin, U destination) {
        return super.isNeighbor(checkBoundsAndWrap(origin), checkBoundsAndWrap(destination));
    }

    @Override
    public boolean itemAt(U point) {
        return super.itemAt(checkBoundsAndWrap(point));
    }

    @Override
    public boolean itemAt(U point, T item) {
        return super.itemAt(checkBoundsAndWrap(point), item);
    }

    @Override
    public List<U> findPath(U from, U to) {
        return super.findPath(checkBoundsAndWrap(from), checkBoundsAndWrap(to));
    }

    protected U checkBoundsAndWrap(U point){
        if (!inBounds(point)){
            throw new PointOutOfBoundsException();
        }
        return wrapPoint(point);
    }

    public abstract boolean inBounds(U point);
    protected abstract U wrapPoint(U point);

    public abstract Iterable<U> possibleLocations();
}
