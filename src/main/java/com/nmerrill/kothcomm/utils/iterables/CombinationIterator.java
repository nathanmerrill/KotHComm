package com.nmerrill.kothcomm.utils.iterables;

public final class CombinationIterator extends PoolIterator {

    public CombinationIterator(int size, int max){
        super(size, max);
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

        int digitMax = max - size + index;
        if (digit == digitMax) {
            nextDigits(index - 1);
            digits.set(index, digits.get(index - 1) + 1);
        } else {
            digits.set(index, digit + 1);
        }
    }

    @Override
    public boolean isFinished() {
        int offset = max - size;
        for (int i = 0; i < size; i++){
            if (digits.get(i) != offset + i){
                return false;
            }
        }
        return true;
    }
}
