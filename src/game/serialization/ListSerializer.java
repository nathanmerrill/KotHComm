package game.serialization;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListSerializer<T> implements Serializer<List<T>> {
    private final Serializer<T> itemSerializer;
    private final String separator;
    public ListSerializer(Serializer<T> itemSerializer){
        this.itemSerializer = itemSerializer;
        char[] arr = new char[separationLevel()];
        Arrays.fill(arr,',');
        separator = new String(arr);
    }

    @Override
    public int separationLevel() {
        return itemSerializer.separationLevel() + 1;
    }

    @Override
    public List<T> deserialize(String representation) {
        return Arrays.asList(representation.split(separator))
                .stream().map(itemSerializer::deserialize).collect(Collectors.toList());
    }

    @Override
    public String serialize(List<T> value) {
        return value.stream().map(itemSerializer::serialize).collect(Collectors.joining(separator));
    }

    @Override
    public String define() {
        return "List("+ itemSerializer.define()+","+(separationLevel())+")";
    }
}