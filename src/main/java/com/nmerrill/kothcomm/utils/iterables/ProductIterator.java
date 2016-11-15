package com.nmerrill.kothcomm.utils.iterables;


public final class ProductIterator extends PoolIterator {

    public ProductIterator(int size, int max){
        super(size, max);
    }

    @Override
    public boolean repeats() {
        return true;
    }

    protected void nextDigits(int index) {
        if (index < 0){
            return;
        }
        int digit = digits.get(index);
        int maxDigit = max - 1;
        if (digit == maxDigit) {
            nextDigits(index -1);
            digits.set(index, 0);
        } else {
            digits.set(index, digit+1);
        }
    }

    @Override
    public boolean isFinished() {
        return digits.allSatisfy(i -> i == max);
    }
}
