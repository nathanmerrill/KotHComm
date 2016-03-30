package utils.iterables;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public final class Tools {
    private Tools(){}
    public static <T> List<T> iterToList(Iterator<T> iter){
        ArrayList<T> list = new ArrayList<>();
        while (iter.hasNext()){
            list.add(iter.next());
        }
        return list;
    }
    public static List<Integer> range(int max){
        List<Integer> range = new ArrayList<>();
        for (int i = 0; i < max; i++){
            range.add(i);
        }
        return range;
    }
    public static boolean inBounds(int num, int min, int max){
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

    public static <T> List<List<T>> partition(List<T> list, int wordSize){
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i+=wordSize){
            partitions.add(list.subList(i, i+wordSize));
        }

        return partitions;
    }

}
