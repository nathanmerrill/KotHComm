package com.ppcg.kothcomm.communication.serialization;

public class OptionalSerializer<T> implements Serializer<T> {
    private final Serializer<T> serializer;
    public OptionalSerializer(Serializer<T> serializer){
        this.serializer = serializer;
    }

    @Override
    public String serialize(T value) {
        if (value == null){
            return "";
        }
        return serializer.serialize(value);
    }

    @Override
    public T deserialize(String representation) {
        if (representation.trim().isEmpty()){
            return null;
        }
        return serializer.deserialize(representation);
    }
}
