package com.ppcg.kothcomm.communication.serialization;

public class IntegerSerializer implements Serializer<Integer> {
    @Override
    public String serialize(Integer value) {
        return value.toString();
    }

    @Override
    public Integer deserialize(String representation) {
        return Integer.parseInt(representation.trim());
    }
}