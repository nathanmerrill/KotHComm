package com.ppcg.kothcomm.utils.iterables;


class ProductIterable<T> extends SubsequenceIterable<T> {

    public ProductIterable(Iterable<T> iter, int length){
        super(iter, length);
    }

    public ProductIterable(Iterable<T> iter){
        this(iter, 2);
    }

    protected void nextDigits(int index) {
        if  (index < 0){
            hasNext = false;
            return;
        }
        int digit = digits.get(index);
        int maxDigit = pool.size() - 1;
        if (digit == maxDigit) {
            nextDigits(index -1);
            digits.set(index, 0);
        } else {
            digits.set(index, digit+1);
        }
    }

}
