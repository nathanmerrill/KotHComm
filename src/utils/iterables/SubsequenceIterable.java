package utils.iterables;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class SubsequenceIterable<T> implements Iterable<List<T>> {
    protected final List<T> pool;
    protected final List<Integer> digits;
    protected boolean hasNext;

    public SubsequenceIterable(Iterator<T> iter, int length) {
        this.pool = Tools.iterToList(iter);
        digits = Tools.range(length);
        hasNext = true;
    }

    private class SubsequenceIterator implements Iterator<List<T>> {

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public List<T> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            List<T> toReturn = digits.stream().map(pool::get).collect(Collectors.toList());
            nextDigits(digits.size() - 1);
            return toReturn;
        }
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new SubsequenceIterator();
    }

    protected abstract void nextDigits(int index);

    public Stream<List<T>> stream(){
        return StreamSupport.stream(this.spliterator(), false);
    }
}
