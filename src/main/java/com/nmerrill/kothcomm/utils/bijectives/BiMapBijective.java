package com.nmerrill.kothcomm.utils.bijectives;

import org.eclipse.collections.api.bimap.BiMap;

public class BiMapBijective<T, U> implements Bijective<T, U> {
    private final BiMap<T, U> map;
    public BiMapBijective(BiMap<T, U> map){
        this.map = map;
    }

    @Override
    public U to(T one) {
        return map.get(one);
    }

    @Override
    public T from(U one) {
        return map.inverse().get(one);
    }
}
