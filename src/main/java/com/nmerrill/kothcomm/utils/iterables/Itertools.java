package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.List;
import java.util.Random;

public final class Itertools {
    private Itertools(){}
    public static <T> SubsequenceIterable<T> combination(Iterable<T> list, int size){
        return new CombinationIterable<>(list, size);   
    }
    public static <T> SubsequenceIterable<T> combinationWithReplacement(Iterable<T> list, int size){
        return new CombinationWithReplacementIterable<>(list, size);
    }
    public static <T> SubsequenceIterable<T> permutation(Iterable<T> list, int size){
        return new PermutationIterable<>(list, size);
    }
    public static <T> SubsequenceIterable<T> product(Iterable<T> list, int size){
        return new ProductIterable<>(list, size);
    }
    public static <T> SubsequenceIterable<T> combination(Iterable<T> list){
        return new CombinationIterable<>(list, 2);
    }
    public static <T> SubsequenceIterable<T> combinationWithReplacement(Iterable<T> list){
        return new CombinationWithReplacementIterable<>(list, 2);
    }
    public static <T> SubsequenceIterable<T> permutation(Iterable<T> list){
        return new PermutationIterable<>(list, 2);
    }
    public static <T> SubsequenceIterable<T> product(Iterable<T> list){
        return new ProductIterable<>(list, 2);
    }

    public static MutableIntList range(int min, int max, int step){
        MutableIntList range = IntLists.mutable.empty();
        for (int i = min; i < max; i += step){
            range.add(i);
        }
        return range;
    }

    public static MutableIntList range(int min, int max){
        return range(min, max, 1);
    }

    public static MutableIntList range(int max){
        return range(0, max);
    }

    public static <T> T sample(List<T> list, Random random){
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T sample(List<T> list){
        return sample(list, new Random());
    }
}
