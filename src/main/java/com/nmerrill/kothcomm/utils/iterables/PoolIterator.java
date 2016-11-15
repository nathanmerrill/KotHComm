package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.Arrays;

public abstract class PoolIterator {
    protected final MutableIntList digits;
    protected final int size;
    protected final int max;
    public PoolIterator(int size, int max){
        assert (size <= max);
        digits = IntLists.mutable.empty();
        this.size = size;
        this.max = max;
    }

    public MutableIntList next(){
        if (digits.isEmpty()){
            if (repeats()){
                int[] arr = new int[size];
                Arrays.fill(arr, 0);
                this.digits.addAll(IntLists.mutable.of(arr));
            } else {
                this.digits.addAll(Itertools.range(size));
            }
            return this.digits;
        }
        nextDigits(size-1);
        return this.digits;
    }

    public abstract boolean repeats();

    public abstract boolean isFinished();

    protected abstract void nextDigits(int position);
}
