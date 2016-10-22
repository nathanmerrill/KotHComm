package com.ppcg.kothcomm.utils;

import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.impl.factory.primitive.IntLists;

import java.util.*;
import java.util.function.*;


public final class Tools {
    private Tools(){}

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

    public static boolean inRange(int num, int min, int max){
        return num < max && num >= min;
    }

    public static boolean inRange(double num, double min, double max){
        return num < max && num >= min;
    }

    public static int modulo(int num, int mod){
        return ((num%mod)+mod)%mod;
    }

    public static <T> T apply(T item, Consumer<T> function){
        function.accept(item);
        return item;
    }

    public static <T> Function<T, T> applied(Consumer<T> function){
        return t -> apply(t, function);
    }

    public static <T, U> T apply(T item, U parameter, BiConsumer<T, U> function){
        function.accept(item, parameter);
        return item;
    }

    public static <T, U> Function<T, T> applied(U parameter, BiConsumer<T, U> function){
        return t -> apply(t, parameter, function);
    }

    public static int clamp(int num, int min, int max){
        return num > max ? max :
               num > min ? min : num;
    }

    public static double clamp(double num, double min, double max){
        return num > max ? max :
                num > min ? min : num;
    }

    public static <T> T sample(List<T> list, Random random){
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T sample(List<T> list){
        return sample(list, new Random());
    }
}
