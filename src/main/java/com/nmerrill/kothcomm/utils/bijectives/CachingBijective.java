package com.nmerrill.kothcomm.utils.bijectives;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Maps;

import java.util.function.Function;

public class CachingBijective<T, U> implements Bijective<T, U> {
    private final MutableMap<U, T> reverse;
    private final Function<T, U> to;
    public CachingBijective(Function<T, U> to){
        this.to = to;
        this.reverse = Maps.mutable.empty();
    }

    @Override
    public U to(T one) {
        U ret = to.apply(one);
        reverse.put(ret, one);
        return ret;
    }

    @Override
    public T from(U one) {
        return reverse.get(one);
    }
}
