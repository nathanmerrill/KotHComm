package utils.iterables;


import utils.Pair;

import java.util.Iterator;

public class WithIndex<T> implements Iterable<Pair<Integer, T>>{
    private final Iterable<T> iterable;
    public WithIndex(Iterable<T> iterable){
        this.iterable = iterable;
    }
    public class IndexIterator implements Iterator<Pair<Integer, T>> {
        private final Iterator<T> iterator;
        private int index;
        public IndexIterator(){
            this.iterator = iterable.iterator();
            this.index = -1;
        }
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Pair<Integer, T> next() {
            index++;
            return new Pair<>(index, iterator.next());
        }
    }

    @Override
    public Iterator<Pair<Integer, T>> iterator() {
        return new IndexIterator();
    }
}
