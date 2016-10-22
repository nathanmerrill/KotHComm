package com.ppcg.kothcomm.utils.iterables;

public class CombinationWithReplacementIterable<T> extends SubsequenceIterable<T> {

    public CombinationWithReplacementIterable(Iterable<T> iter, int length){
        super(iter, length);
    }

    public CombinationWithReplacementIterable(Iterable<T> iter){
        this(iter, 2);
    }

    protected void nextDigits(int index) {
        if (index < 0) {
            return;
        }
        int digit = digits.get(index);
        int digitMax = index + 1 == digits.size() ? pool.size() - 1 : index + 1;
        if (digit > digitMax) {
            nextDigits(index - 1);
            digits.set(index, digits.get(index - 1));
        } else {
            digits.set(index, digit + 1);
        }
    }
}
