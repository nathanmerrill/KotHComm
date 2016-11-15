package com.nmerrill.kothcomm.utils.iterables;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.List;
import java.util.Random;

public final class Itertools {
    private Itertools(){}
    
    
    public static <T> SubsequenceIterable<T> combination(Iterable<T> iter, int max){
        MutableList<T> list = Lists.mutable.ofAll(iter);
        return new SubsequenceIterable<>(list, () -> new CombinationIterator(list.size(), max));
    }
    public static <T> SubsequenceIterable<T> combinationWithReplacement(Iterable<T> iter, int max){
        MutableList<T> list = Lists.mutable.ofAll(iter);
        return new SubsequenceIterable<>(list, () -> new CombinationWithReplacementIterator(list.size(), max));
    }
    public static <T> SubsequenceIterable<T> permutation(Iterable<T> iter, int max){
        MutableList<T> list = Lists.mutable.ofAll(iter);
        return new SubsequenceIterable<>(list, () -> new PermutationIterator(list.size(), max));
    }
    public static <T> SubsequenceIterable<T> product(Iterable<T> iter, int max){
        MutableList<T> list = Lists.mutable.ofAll(iter);
        return new SubsequenceIterable<>(list, () -> new ProductIterator(list.size(), max));
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
