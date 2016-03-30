package game.serialization;


import utils.iterables.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TupleSerializer implements Serializer<Map<String, Object>>{
    private final List<Pair<String, Serializer<Object>>> attributes;
    private final HashMap<String, Serializer<Object>> attributeMap;
    private final int separationLevel;
    private final String separator;
    public TupleSerializer(List<Pair<String, Serializer<Object>>> attributes){
        this.attributes = attributes;
        attributeMap = new HashMap<>();
        for (Pair<String, Serializer<Object>> attribute: attributes){
            attributeMap.put(attribute.first(), attribute.second());
        }
        separationLevel = attributeMap.values().stream().map(Serializer::separationLevel).max(Integer::compare).get()+1;
        char[] arr = new char[separationLevel];
        Arrays.fill(arr, ',');
        separator = new String(arr);
    }

    @Override
    public int separationLevel() {
        return separationLevel;
    }

    @Override
    public String serialize(Map<String, Object> value) {
        return value.entrySet().stream()
                .map(entry -> attributeMap.get(entry.getKey()).serialize(entry.getValue()))
                .collect(Collectors.joining(separator));
    }

    @Override
    public Map<String, Object> deserialize(String representation) {
        String[] parts = representation.split(separator);
        Map<String, Object> deserialized = new HashMap<>();
        for (int i = 0; i < attributes.size(); i++){
            deserialized.put(parts[i], attributes.get(i).second().deserialize(representation));
        }
        return deserialized;
    }

    @Override
    public String define() {
        StringBuilder definition = new StringBuilder("Tuple(");
        for (Pair<String, Serializer<Object>> serializer: attributes){
            definition.append(serializer.first());
            definition.append(":");
            definition.append(serializer.second().define());
            definition.append(",");
        }
        definition.deleteCharAt(definition.length()-1);
        definition.append(")");
        return definition.toString();
    }

}