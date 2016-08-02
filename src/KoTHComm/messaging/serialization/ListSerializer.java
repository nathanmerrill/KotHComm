package KoTHComm.messaging.serialization;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListSerializer<T> implements Serializer<List<T>> {

    private final Serializer<T> itemSerializer;
    public ListSerializer(Serializer<T> itemSerializer){
        this.itemSerializer = itemSerializer;
    }

    @Override
    public int separationLevel() {
        return itemSerializer.separationLevel() + 1;
    }

    @Override
    public List<T> deserialize(String representation) {
        return Arrays.stream(representation.split(getSeparator()))
                .map(itemSerializer::deserialize).collect(Collectors.toList());
    }

    @Override
    public String serialize(List<T> value) {
        return value.stream().map(itemSerializer::serialize).collect(Collectors.joining(getSeparator()));
    }

}