package com.nmerrill.kothcomm.communication.serialization;


import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

public final class PairSerializer<T, U> implements Serializer<Pair<T, U>>{
    private final Serializer<T> serializerFirst;
    private final Serializer<U> serializerSecond;
    public PairSerializer(Serializer<T> serializerFirst, Serializer<U> serializerSecond){
        this.serializerFirst = serializerFirst;
        this.serializerSecond = serializerSecond;
    }
    @Override
    public String serialize(Pair<T, U> value) {
        return serializerFirst.serialize(value.getOne())+getSeparator()+serializerSecond.serialize(value.getTwo());
    }

    @Override
    public Pair<T, U> deserialize(String representation) {
        String[] values = representation.split(getSeparator());
        return Tuples.pair(serializerFirst.deserialize(values[0]), serializerSecond.deserialize(values[1]));
    }

    @Override
    public int separationLevel() {
        return Math.max(serializerFirst.separationLevel(), serializerSecond.separationLevel())+1;
    }
}
