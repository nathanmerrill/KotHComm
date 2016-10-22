package com.ppcg.kothcomm.communication.serialization;

public class StringSerializer implements Serializer<String>{
    @Override
    public String deserialize(String representation) {
        return representation;
    }

    @Override
    public String serialize(String value) {
        return value;
    }
}