package com.nmerrill.kothcomm.utils;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Functions {
    private Functions(){}
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
}
