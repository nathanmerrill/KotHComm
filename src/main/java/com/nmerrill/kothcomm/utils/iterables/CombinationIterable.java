package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.primitive.MutableIntList;

public final class CombinationIterable<T> extends SubsequenceIterable<T> {

    public CombinationIterable(Iterable<T> iter, int length){
        super(iter, length);
    }

    protected boolean nextDigits(int index, MutableIntList digits) {
        int digit = digits.get(index);

        int digitMax = getSize() - digits.size() + index;
        if (digit >= digitMax) {
            if (index == 0){
                return false;
            }
            if (!nextDigits(index - 1, digits)){
                return false;
            }
            digits.set(index, digits.get(index - 1) + 1);
        } else {
            digits.set(index, digit + 1);
        }
        return true;
    }
}
