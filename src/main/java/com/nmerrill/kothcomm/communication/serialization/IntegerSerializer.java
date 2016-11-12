package com.nmerrill.kothcomm.communication.serialization;

public final class IntegerSerializer implements Serializer<Integer> {
    @Override
    public String serialize(Integer value) {
        return value.toString();
    }

    @Override
    public Integer deserialize(String representation) {
        return Integer.parseInt(representation.trim());
    }
}