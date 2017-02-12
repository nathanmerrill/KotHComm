package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.LazyIntIterable;
import org.eclipse.collections.api.block.procedure.primitive.IntProcedure;
import org.eclipse.collections.api.iterator.IntIterator;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.lazy.primitive.AbstractLazyIntIterable;

import java.util.List;
import java.util.Random;

public final class Itertools {
    private Itertools(){}
    
    
    public static <T> SubsequenceIterable<T> combination(Iterable<T> iter, int length){
        MutableList<T> list = Lists.mutable.ofAll(iter);
        return new SubsequenceIterable<>(list, () -> new CombinationIterator(list.size(), length));
    }
    public static <T> SubsequenceIterable<T> combinationWithReplacement(Iterable<T> iter, int length){
        MutableList<T> list = Lists.mutable.ofAll(iter);
        return new SubsequenceIterable<>(list, () -> new CombinationWithReplacementIterator(list.size(), length));
    }
    public static <T> SubsequenceIterable<T> permutation(Iterable<T> iter, int length){
        MutableList<T> list = Lists.mutable.ofAll(iter);
        return new SubsequenceIterable<>(list, () -> new PermutationIterator(list.size(), length));
    }
    public static <T> SubsequenceIterable<T> product(Iterable<T> iter, int length){
        MutableList<T> list = Lists.mutable.ofAll(iter);
        return new SubsequenceIterable<>(list, () -> new ProductIterator(list.size(), length));
    }
    public static <T> SubsequenceIterable<T> combination(Iterable<T> iter){
        return combination(iter, 2);
    }
    public static <T> SubsequenceIterable<T> combinationWithReplacement(Iterable<T> iter){
        return combinationWithReplacement(iter, 2);
    }
    public static <T> SubsequenceIterable<T> permutation(Iterable<T> iter){
        return permutation(iter, 2);
    }
    public static <T> SubsequenceIterable<T> product(Iterable<T> iter){
        return product(iter, 2);
    }

    public static LazyIntIterable range(int min, int max, int step){
        return new AbstractLazyIntIterable(){
            @Override
            public IntIterator intIterator() {
                return new IntIterator() {
                    int current = min;
                    @Override
                    public int next() {
                        int ret = current;
                        current += step;
                        return ret;
                    }

                    @Override
                    public boolean hasNext() {
                        return current < max;
                    }
                };
            }

            @Override
            public void each(IntProcedure procedure) {
                for (int i = min; i < max; i+=step){
                    procedure.accept(i);
                }
            }
        };

    }

    public static LazyIntIterable range(int min, int max){
        return range(min, max, 1);
    }

    public static LazyIntIterable range(int max){
        return range(0, max);
    }

    public static <T> T sample(List<T> list, Random random){
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T sample(List<T> list){
        return sample(list, new Random());
    }
}
