package com.ppcg.kothcomm.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A pair/tuple of two arbitrary elements.
 * A pair is immutable, so you can ensure that the pair you have will never change.
 * @param <T>
 * @param <U>
 */
public final class Pair<T, U> {
    private final T first;
    private final U second;

    /**
     * Creates a Pair
     * @param first The first element in the pair
     * @param second The second element in the pair
     */
    public Pair(T first, U second){
        this.first = first;
        this.second = second;
    }

    /**
     * Copy constructor
     * @param pair The pair you want to make a copy of
     */
    public Pair(Pair<T, U> pair){
        this(pair.first, pair.second);
    }

    /**
     * Creates a Pair from a Map Entry
     * @param entry The entry to copy from
     */
    public Pair(Map.Entry<T, U> entry){
        this(entry.getKey(), entry.getValue());
    }

    /**
     * @return The first element in the pair
     */
    public T first() {
        return first;
    }

    /**
     * @return The second element in the pair
     */
    public U second() {
        return second;
    }

    /**
     * Tests if the pair is equal to another object. Elements in a pair are tested for equality using .equals()
     * @param o The object to check for equality
     * @return true if the objects are of the same type, and contain equal elements, false otherwise
     */
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

    /**
     * Used to hash the pair.  Two equal elements will have the same hash
     * @return The hash of the Pair
     */
    @Override
    public int hashCode() {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }

    /**
     * Maps the pair to a new pair by applying two functions to its elements
     * @param map1 A function that accepts the first element in the pair
     * @param map2 A function that accepts the second element in the pair
     * @param <V> The type of the first element in the new pair
     * @param <W> The type of the second element in the new pair
     * @return A new pair after applying the two functions to its elements
     */
    public <V, W> Pair<V, W> map(Function<T, V> map1, Function<U, W> map2){
        return new Pair<>(map1.apply(first), map2.apply(second));
    }

    /**
     * Does the same thing as map(Function, Function), however, it accepts a Pair of functions
     * @param mapping The pair of functions that can convert the element
     * @param <V> The type of the first element in the new pair
     * @param <W> The type of the second element in the new pair
     * @return A new pair after applying the paired functions to its elements
     */
    public <V, W> Pair<V, W> map(Pair<Function<T, V>, Function<U, W>> mapping){
        return map(mapping.first, mapping.second);
    }

    /**
     * @return A string in "(obj1, obj2)" format
     */
    @Override
    public String toString() {
        return "("+first.toString()+", "+second.toString()+")";
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
