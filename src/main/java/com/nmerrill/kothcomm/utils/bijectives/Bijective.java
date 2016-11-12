package com.nmerrill.kothcomm.utils.bijectives;

public interface Bijective<T, U> {
    U to(T one);
    T from(U one);
}
