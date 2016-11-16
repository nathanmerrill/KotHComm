package com.nmerrill.kothcomm.utils.iterables;

public final class CombinationIterator extends PoolIterator {

    public CombinationIterator(int maxDigit, int listSize){
        super(maxDigit, listSize);
    }

    @Override
    public boolean repeats() {
        return false;
    }

    protected void nextDigits(int index) {
        if (index < 0){
            return;
        }
        int digit = digits.get(index);

        int digitMax = maxDigit - listSize + index;

        if (digit == digitMax) {
            nextDigits(index - 1);
            digits.set(index, digits.get(index - 1) + 1);
        } else {
            digits.set(index, digit + 1);
        }
    }

    @Override
    public boolean isFinished() {
        int offset = maxDigit - listSize;
        for (int i = 0; i < listSize; i++){
            if (digits.get(i) != offset + i){
                return false;
            }
        }
        return true;
    }
}
