package com.nmerrill.kothcomm.communication.serialization;

public final class VoidSerializer implements Serializer<String>{
    @Override
    public String serialize(String value) {
        return "";
    }

    @Override
    public String deserialize(String representation) {
        return "";
    }
}
