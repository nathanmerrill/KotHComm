package KoTHComm.utils.iterables;

import java.util.Iterator;
import java.util.function.Supplier;

public class SupplierIterable<T> implements Iterable<T> {
    private final Supplier<T> supplier;
    private final int iterations;
    public SupplierIterable(Supplier<T> supplier, int iterations){
        this.supplier = supplier;
        this.iterations = iterations;
    }
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int currentIteration = 0;
            @Override
            public boolean hasNext() {
                return currentIteration < iterations;
            }

            @Override
            public T next() {
                currentIteration++;
                return supplier.get();
            }
        };
    }
}
