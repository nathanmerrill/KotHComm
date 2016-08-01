package utils;

import java.util.*;
import java.util.function.*;


public final class Tools {
    private Tools(){}
    public static <T> List<T> iterToList(Iterator<T> iter){
        ArrayList<T> list = new ArrayList<>();
        while (iter.hasNext()){
            list.add(iter.next());
        }
        return list;
    }
    public static List<Integer> range(int min, int max, int step){
        List<Integer> range = new ArrayList<>();
        for (int i = min; i < max; i += step){
            range.add(i);
        }
        return range;
    }
    public static List<Integer> range(int min, int max){
        return range(min, max, 1);
    }
    public static List<Integer> range(int max){
        return range(0, max);
    }

    public static boolean inRange(int num, int min, int max){
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
            partitions.add(list.subList(i, Math.min(i+wordSize, list.size())));
        }
        return partitions;
    }

    public static <T> T sample(Collection<T> collection, Random random){
        return collection.stream().skip(random.nextInt(collection.size())).findFirst().orElse(null);
    }

    public static <T> T sample(Collection<T> collection){
        return sample(collection, new Random());
    }

    public static <T> T sample(List<T> list, Random random){
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T sample(List<T> list){
        return sample(list, new Random());
    }

    public static <T> List<T> sample(Collection<T> list, int count, Random random){
        List<T> shuffled = new ArrayList<>(list);
        Collections.shuffle(shuffled, random);
        return shuffled.subList(0, count);
    }

    public static <T> List<T> sample(Collection<T> list, int count){
        return sample(list, count, new Random());
    }

}
