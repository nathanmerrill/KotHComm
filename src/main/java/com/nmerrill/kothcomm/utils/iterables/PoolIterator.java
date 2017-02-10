package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.Arrays;

public abstract class PoolIterator {
    protected final MutableIntList digits;
    protected final int listSize;
    protected final int maxDigit;
    public PoolIterator(int maxDigit, int listSize){
        if (listSize > maxDigit){
            throw new RuntimeException("Max digit must be smaller than the list size");
        }
        digits = IntLists.mutable.empty();
        this.listSize = listSize;
        this.maxDigit = maxDigit;
    }

    public MutableIntList next(){
        if (digits.isEmpty()){
            if (repeats()){
                int[] arr = new int[listSize];
                Arrays.fill(arr, 0);
                this.digits.addAll(IntLists.mutable.of(arr));
            } else {
                this.digits.addAll(Itertools.range(listSize));
            }
            return this.digits;
        }
        nextDigits(listSize -1);
        return this.digits;
    }

    public abstract boolean repeats();

    public abstract boolean isFinished();

    protected abstract void nextDigits(int position);
}
