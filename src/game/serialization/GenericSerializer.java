package game.serialization;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GenericSerializer implements Serializer<Map<String, Object>> {
    private final HashMap<Class, Serializer> serializers;
    private final LinkedHashMap<Class, String> names;
    public GenericSerializer(){
        serializers = new HashMap<>();
        names = new LinkedHashMap<>();
    }

    public <T> void addSerializer(String name, Class<T> clazz, Serializer<? super T> serializer){
        serializers.put(clazz, serializer);
        names.put(clazz, name);
    }

    @Override
    public Map<String, Object> deserialize(String representation) {
        return null;
    }

    @Override
    public String define() {
        StringBuilder definition = new StringBuilder();
        for (Map.Entry<Class, String> serializer: names.entrySet()){
            definition.append(serializer.getValue());
            definition.append(":");
            definition.append(serializers.get(serializer.getKey()).define());
            definition.append(",");
        }
        return definition.toString();
    }

    @Override
    public int separationLevel() {
        return serializers.values().stream().mapToInt(Serializer::separationLevel).max().getAsInt()+1;
    }

    public String serialize(Map<String, Object> objects){
        for (Class clazz: names.keySet()){

        }
        return "";
    }
}
