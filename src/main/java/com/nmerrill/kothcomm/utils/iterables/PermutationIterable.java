package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.impl.factory.SortedSets;

public final class PermutationIterable<T> extends SubsequenceIterable<T> {

    private final MutableSortedSet<Integer> availableDigits;

    public PermutationIterable(Iterable<T> iter, int length){
        super(iter, length);
        this.availableDigits = SortedSets.mutable.withAll(Itertools.range(length, getSize()).collect(i->i));
    }

    public PermutationIterable(Iterable<T> iter){
        this(iter, 2);
    }

    protected boolean nextDigits(int index, MutableIntList digits) {
        if (index < 0) {
            return false;
        }
        int digit = digits.get(index);
        MutableSortedSet<Integer> nextAvailable = availableDigits.tailSet(digit);
        availableDigits.add(digit);
        int nextDigit;
        if (nextAvailable.isEmpty()) {
            if (!nextDigits(index - 1, digits)){
                return false;
            }
            nextDigit = availableDigits.first();
        } else {
            nextDigit = nextAvailable.first();
        }
        digits.set(index, nextDigit);
        availableDigits.remove(nextDigit);
        return true;
    }
}
