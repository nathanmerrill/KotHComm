package game.maps.gridmaps;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class GridIterable implements Iterable<GridPoint> {
    private final int width, height;
    public GridIterable(int width, int height){
        this.width = width;
        this.height = height;
    }
    private final class GridIterator implements Iterator<GridPoint>{
        private int x, y;

        private GridIterator() {
            this.x = 0;
            this.y = 0;
        }

        @Override
        public GridPoint next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            GridPoint ret = new GridPoint(x, y);
            x++;
            if (x == width) {
                y++;
                x = 0;
            }
            return ret;
        }

        @Override
        public boolean hasNext() {
            return y < height;
        }
    }

    @Override
    public Iterator<GridPoint> iterator() {
        return new GridIterator();
    }
}
