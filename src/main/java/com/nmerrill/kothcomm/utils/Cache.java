package com.nmerrill.kothcomm.utils;

import java.util.function.Supplier;

public class Cache<T> {
    T item;
    public void breakCache(){
        item = null;
    }
    public T get(Supplier<T> supplier){
        if (item == null){
            item = supplier.get();
        }
        return item;
    }
}
