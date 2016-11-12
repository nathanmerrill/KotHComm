package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class SubsequenceIterable<T> implements Iterable<MutableList<T>> {
    private final MutableList<T> pool;
    private final int length;

    public SubsequenceIterable(Iterable<T> iter, int length) {
        this.pool = Lists.mutable.ofAll(iter);
        this.length = length;
    }

    private class SubsequenceIterator implements Iterator<MutableList<T>> {
        private final MutableIntList digits;
        private boolean hasNext;
        public SubsequenceIterator(){
            digits = Itertools.range(length);
            hasNext = true;
        }
        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public MutableList<T> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            MutableList<T> toReturn = digits.collect(pool::get).toList();
            hasNext = nextDigits(digits.size() - 1, digits);
            return toReturn;
        }
    }

    public int getSize(){
        return pool.size();
    }

    @Override
    public Iterator<MutableList<T>> iterator() {
        return new SubsequenceIterator();
    }

    protected abstract boolean nextDigits(int index, MutableIntList digits);

    public Stream<MutableList<T>> stream(){
        return StreamSupport.stream(this.spliterator(), false);
    }

    public MutableList<MutableList<T>> toList() {
        return Lists.mutable.ofAll(this);
    }

    public MutableSet<MutableList<T>> toSet() {
        return Sets.mutable.ofAll(this);
    }
}
