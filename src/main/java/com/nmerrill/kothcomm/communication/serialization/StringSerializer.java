package com.nmerrill.kothcomm.communication.serialization;

public final class StringSerializer implements Serializer<String>{
    @Override
    public String deserialize(String representation) {
        return representation;
    }

    @Override
    public String serialize(String value) {
        return value;
    }
}