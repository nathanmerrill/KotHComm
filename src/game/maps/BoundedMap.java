package game.maps;


public abstract class BoundedMap<T, U extends MapPoint> extends GameMap<T, U>{

    @Override
    public T get(U point) {
        if (!inBounds(point)){
            throw new PointOutOfBoundsException();
        }
        return super.get(wrapPoint(point));
    }

    @Override
    public void put(U point, T item) {
        if (!inBounds(point)){
            throw new PointOutOfBoundsException();
        }
        super.put(wrapPoint(point), item);
    }

    public abstract boolean inBounds(U point);
    protected abstract U wrapPoint(U point);

    public abstract Iterable<U> possibleLocations();
}
