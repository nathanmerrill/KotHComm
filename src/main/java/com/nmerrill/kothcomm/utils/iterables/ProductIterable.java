package com.nmerrill.kothcomm.utils.iterables;


import org.eclipse.collections.api.list.primitive.MutableIntList;

public final class ProductIterable<T> extends SubsequenceIterable<T> {

    public ProductIterable(Iterable<T> iter, int length){
        super(iter, length);
    }

    protected boolean nextDigits(int index, MutableIntList digits) {
        if  (index < 0){
            return false;
        }
        int digit = digits.get(index);
        int maxDigit = getSize() - 1;
        if (digit == maxDigit) {
            if (!nextDigits(index -1, digits)){
                return false;
            }
            digits.set(index, 0);
        } else {
            digits.set(index, digit+1);
        }
        return true;
    }

}
