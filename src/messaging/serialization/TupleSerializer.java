package messaging.serialization;


import utils.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TupleSerializer implements Serializer<Map<String, Object>>{
    private final List<Pair<String, Serializer<Object>>> attributes;
    private final HashMap<String, Serializer<Object>> attributeMap;
    private final int separationLevel;
    public TupleSerializer(List<Pair<String, Serializer<Object>>> attributes){
        this.attributes = attributes;
        attributeMap = new HashMap<>();
        for (Pair<String, Serializer<Object>> attribute: attributes){
            attributeMap.put(attribute.first(), attribute.second());
        }
        separationLevel = attributeMap.values().stream().map(Serializer::separationLevel).max(Integer::compare).get()+1;

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
            deserialized.put(parts[i], attributes.get(i).second().deserialize(representation));
        }
        return deserialized;
    }

}