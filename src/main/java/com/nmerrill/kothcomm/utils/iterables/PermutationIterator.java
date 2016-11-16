package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.impl.factory.SortedSets;

public final class PermutationIterator extends PoolIterator {

    private final MutableSortedSet<Integer> availableDigits;
    public PermutationIterator(int maxDigit, int listSize){
        super(maxDigit, listSize);
        availableDigits = SortedSets.mutable.withAll(Itertools.range(listSize, maxDigit).collect(i->i));
    }

    @Override
    public boolean repeats() {
        return false;
    }

    protected void nextDigits(int index) {
        if (index < 0) {
            return;
        }
        int digit = digits.get(index);
        availableDigits.add(digit);
        MutableSortedSet<Integer> nextAvailable = availableDigits.tailSet(digit+1);
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

    @Override
    public boolean isFinished() {
        for (int i = 0; i < listSize; i++){
            if (digits.get(i) != maxDigit - i -1){
                return false;
            }
        }
        return true;
    }
}
