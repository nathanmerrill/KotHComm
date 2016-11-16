package com.nmerrill.kothcomm.utils.iterables;

public final class CombinationWithReplacementIterator extends PoolIterator {

    public CombinationWithReplacementIterator(int maxDigit, int listSize){
        super(maxDigit, listSize);
    }

    protected void nextDigits(int index) {
        if (index < 0) {
            return;
        }
        int digit = digits.get(index);
        int digitMax = (index + 1 == listSize) ? maxDigit - 1 : digits.get(index + 1);
        if (digit == digitMax) {
            nextDigits(index - 1);
            digits.set(index, digits.get(index - 1));
        } else {
            digits.set(index, digit + 1);
        }
    }

    @Override
    public boolean repeats() {
        return true;
    }

    @Override
    public boolean isFinished() {
        return digits.allSatisfy(i -> i == maxDigit);
    }
}
