package KoTHComm.utils.iterables;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Reversed<T> implements Iterable<T>{
    private final List<T> list;
    public Reversed(Iterable<T> iterable){
        this(iterable.iterator());
    }
    public Reversed(Iterator<T> iterator){
        list = new ArrayList<>();
        while (iterator.hasNext()){
            list.add(iterator.next());
        }
    }
    public class IndexIterator implements Iterator<T> {
        private int index;
        public IndexIterator(){
            this.index = list.size();
        }
        @Override
        public boolean hasNext() {
            return index > 0;
        }

        @Override
        public T next() {
            index--;
            return list.get(index);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new IndexIterator();
    }
}
