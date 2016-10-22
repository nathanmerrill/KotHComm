package com.ppcg.kothcomm.utils.iterables;

public final class Itertools {
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
}
