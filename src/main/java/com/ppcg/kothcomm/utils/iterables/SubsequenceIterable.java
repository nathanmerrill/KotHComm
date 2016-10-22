package com.ppcg.kothcomm.utils.iterables;

import com.ppcg.kothcomm.utils.Tools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class SubsequenceIterable<T> implements Iterable<MutableList<T>> {
    protected final MutableList<T> pool;
    protected final MutableIntList digits;
    protected boolean hasNext;

    public SubsequenceIterable(Iterable<T> iter, int length) {
        this.pool = Lists.mutable.ofAll(iter);
        digits = Tools.range(length);
        hasNext = true;
    }

    private class SubsequenceIterator implements Iterator<MutableList<T>> {

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
            nextDigits(digits.size() - 1);
            return toReturn;
        }
    }

    @Override
    public Iterator<MutableList<T>> iterator() {
        return new SubsequenceIterator();
    }

    protected abstract void nextDigits(int index);

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
