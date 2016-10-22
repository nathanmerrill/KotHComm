package com.ppcg.kothcomm.communication.serialization;

public class VoidSerializer implements Serializer<String>{
    @Override
    public String serialize(String value) {
        return "";
    }

    @Override
    public String deserialize(String representation) {
        return "";
    }
}
