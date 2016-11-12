package com.nmerrill.kothcomm.utils.bijectives;

import java.util.function.Function;

public class FunctionalBijective<T, U> implements Bijective<T, U> {
    private final Function<T, U> toFunction;
    private final Function<U, T> fromFunction;
    public FunctionalBijective(Function<T, U> toFunction, Function<U, T> fromFunction){
        this.toFunction = toFunction;
        this.fromFunction = fromFunction;
    }

    @Override
    public U to(T one) {
        return toFunction.apply(one);
    }

    @Override
    public T from(U one) {
        return fromFunction.apply(one);
    }
}
