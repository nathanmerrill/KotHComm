package utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Pair<T, U> {
    private final T first;
    private final U second;
    public Pair(T first, U second){
        this.first = first;
        this.second = second;
    }
    public Pair(Pair<T, U> pair){
        this(pair.first, pair.second);
    }

    public Pair(Map.Entry<T, U> entry){
        this(entry.getKey(), entry.getValue());
    }

    public T first() {
        return first;
    }

    public U second() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (first != null ? !first.equals(pair.first) : pair.first != null){
            return false;
        }
        return second != null ? second.equals(pair.second) : pair.second == null;

    }

    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    public <V, W> Pair<V, W> map(Function<T, V> map1, Function<U, W> map2){
        return new Pair<>(map1.apply(first), map2.apply(second));
    }

    public <V, W> Pair<V, W> map(Pair<Function<T, V>, Function<U, W>> mapping){
        return map(mapping.first, mapping.second);
    }

    @Override
    public String toString() {
        return first.toString()+", "+second.toString();
    }

    public Pair<U, T> swap(){
        return new Pair<>(second, first);
    }

    public static <T, U> Map<T, U> toMap(List<Pair<T, U>> list){
        return list.stream().collect(Collectors.toMap(Pair::first, Pair::second));
    }

    public static <T, U> Map<U, T> toReverseMap(List<Pair<T, U>> list){
        return list.stream().collect(Collectors.toMap(Pair::second, Pair::first));
    }
    public static <T, U> List<Pair<T, U>> fromMap(Map<T, U> map){
        return map.entrySet().stream().map(Pair::new).collect(Collectors.toList());
    }

    public static <T> Pair<T, T> fromList(List<T> list){
        return new Pair<>(list.get(0), list.get(1));
    }

    public static <T, U, V> Function<T, Pair<U, V>> fromValue(Function<T, U> keyMap, Function<T, V> valueMap){
        return i -> new Pair<>(keyMap.apply(i), valueMap.apply(i));
    }

    public static <U,V> Function<U, Pair<U, V>> fromValue(Function<U, V> valueMap){
        return i -> new Pair<>(i, valueMap.apply(i));
    }

    public static <T, U> Iterable<Pair<T, U>> zip(Iterable<T> iter1, Iterable<U> iter2){
        return () ->  new Iterator<Pair<T, U>>() {
            final Iterator<T> i1 = iter1.iterator();
            final Iterator<U> i2 = iter2.iterator();
            @Override
            public boolean hasNext() {
                return i1.hasNext() && i2.hasNext();
            }

            @Override
            public Pair<T, U> next() {
                return new Pair<>(i1.next(), i2.next());
            }
        };

    }
}
