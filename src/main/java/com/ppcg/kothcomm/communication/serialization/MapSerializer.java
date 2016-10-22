package com.ppcg.kothcomm.communication.serialization;


import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapSerializer implements Serializer<Map<String, Object>>{
    private final MutableList<Pair<String, Serializer<Object>>> attributes;
    private final MutableMap<String, Serializer<Object>> attributeMap;
    private final int separationLevel;
    public MapSerializer(List<Pair<String, Serializer<Object>>> attributes){
        this.attributes = Lists.mutable.ofAll(attributes);
        this.attributeMap = this.attributes.toMap(Pair::getOne, Pair::getTwo);
        separationLevel = attributeMap.collectInt(Serializer::separationLevel).max()+1;
    }

    @Override
    public int separationLevel() {
        return separationLevel;
    }

    @Override
    public String serialize(Map<String, Object> value) {
        return value.entrySet().stream()
                .map(entry -> attributeMap.get(entry.getKey()).serialize(entry.getValue()))
                .collect(Collectors.joining(getSeparator()));
    }

    @Override
    public Map<String, Object> deserialize(String representation) {
        String[] parts = representation.split(getSeparator());
        Map<String, Object> deserialized = new HashMap<>();
        for (int i = 0; i < attributes.size(); i++){
            deserialized.put(parts[i], attributes.get(i).getTwo().deserialize(representation));
        }
        return deserialized;
    }

}