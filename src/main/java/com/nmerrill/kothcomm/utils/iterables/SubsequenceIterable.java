package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SubsequenceIterable<T> implements Iterable<MutableList<T>> {
    private final MutableList<T> pool;
    private final Supplier<PoolIterator> iteratorSupplier;
    public SubsequenceIterable(MutableList<T> pool, Supplier<PoolIterator> iteratorSupplier) {
        this.pool = pool;
        this.iteratorSupplier = iteratorSupplier;
    }

    private class SubsequenceIterator implements Iterator<MutableList<T>> {
        private final PoolIterator iterator;
        private SubsequenceIterator(PoolIterator iterator){
            this.iterator = iterator;
        }
        @Override
        public boolean hasNext() {
            return iterator.digits.isEmpty() || !iterator.isFinished();
        }

        @Override
        public MutableList<T> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iterator.next().collect(pool::get);
        }

    }

    public int getSize(){
        return pool.size();
    }

    public Stream<MutableList<T>> stream(){
        return StreamSupport.stream(this.spliterator(), false);
    }

    public MutableList<MutableList<T>> toList() {
        return Lists.mutable.ofAll(this);
    }

    @Override
    public Iterator<MutableList<T>> iterator() {
        return new SubsequenceIterator(iteratorSupplier.get());
    }

    public MutableSet<MutableList<T>> toSet() {
        return Sets.mutable.ofAll(this);
    }
}
