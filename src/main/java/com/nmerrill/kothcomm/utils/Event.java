package com.nmerrill.kothcomm.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Event {
    private boolean cancelled;
    public Event(){
        cancelled = false;
    }
    public boolean isCanceled(){
        return cancelled;
    }
    public void cancel(){
        cancelled = true;
    }


    public static <T> Function<T, Boolean> every(int n, Consumer<T> consumer){
        return new Function<T, Boolean>() {
            private int counter = n;
            @Override
            public Boolean apply(T t) {
                counter--;
                consumer.accept(t);
                if (counter == 0) {
                    return false;
                }
                return true;
            }
        };
    }


    public static <T> Function<T, Boolean> after(int n, Consumer<T> consumer){
        return new Function<T, Boolean>() {
            private int counter = n;
            @Override
            public Boolean apply(T t) {
                counter--;
                if (counter == 0) {
                    consumer.accept(t);
                    return false;
                }
                return true;
            }
        };
    }

    public static <T> Function<T, Boolean> forever(Consumer<T> consumer){
        return (T t) -> {
            consumer.accept(t);
            return true;
        };
    }


    public static <T> Function<T, Boolean> once(Consumer<T> consumer){
        return (T t) -> {
            consumer.accept(t);
            return false;
        };
    }



}
