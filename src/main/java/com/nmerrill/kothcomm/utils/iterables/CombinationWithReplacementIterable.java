package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.primitive.MutableIntList;

public final class CombinationWithReplacementIterable<T> extends SubsequenceIterable<T> {

    public CombinationWithReplacementIterable(Iterable<T> iter, int length){
        super(iter, length);
    }

    protected boolean nextDigits(int index, MutableIntList digits) {
        if (index < 0) {
            return false;
        }
        int digit = digits.get(index);
        int digitMax = index + 1 == digits.size() ? getSize() - 1 : index + 1;
        if (digit > digitMax) {
            if (!nextDigits(index - 1, digits)){
                return false;
            }
            digits.set(index, digits.get(index - 1));
        } else {
            digits.set(index, digit + 1);
        }
        return true;
    }
}
