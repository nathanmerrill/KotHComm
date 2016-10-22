package com.ppcg.kothcomm.utils.iterables;

import com.ppcg.kothcomm.utils.Tools;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.impl.factory.SortedSets;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PermutationIterable<T> extends SubsequenceIterable<T> {

    private final MutableSortedSet<Integer> availableDigits;

    public PermutationIterable(Iterable<T> iter, int length){
        super(iter, length);
        this.availableDigits = SortedSets.mutable.withAll(Tools.range(length, pool.size()).collect(i->i));
    }

    public PermutationIterable(Iterable<T> iter){
        this(iter, 2);
    }

    protected void nextDigits(int index) {
        if (index < 0) {
            hasNext = false;
            return;
        }
        int digit = digits.get(index);
        MutableSortedSet<Integer> nextAvailable = availableDigits.tailSet(digit);
        availableDigits.add(digit);
        int nextDigit;
        if (nextAvailable.isEmpty()) {
            nextDigits(index - 1);
            nextDigit = availableDigits.first();
        } else {
            nextDigit = nextAvailable.first();
        }
        digits.set(index, nextDigit);
        availableDigits.remove(nextDigit);
    }
}
