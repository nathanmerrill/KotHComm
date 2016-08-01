package messaging.serialization;

import utils.Pair;

public class PairSerializer<T, U> implements Serializer<Pair<T, U>>{
    private final Serializer<T> serializerFirst;
    private final Serializer<U> serializerSecond;
    public PairSerializer(Serializer<T> serializerFirst, Serializer<U> serializerSecond){
        this.serializerFirst = serializerFirst;
        this.serializerSecond = serializerSecond;
    }
    @Override
    public String serialize(Pair<T, U> value) {
        return serializerFirst.serialize(value.first())+getSeparator()+serializerSecond.serialize(value.second());
    }

    @Override
    public Pair<T, U> deserialize(String representation) {
        String[] values = representation.split(getSeparator());
        return new Pair<>(serializerFirst.deserialize(values[0]), serializerSecond.deserialize(values[1]));
    }

    @Override
    public int separationLevel() {
        return Math.max(serializerFirst.separationLevel(), serializerSecond.separationLevel())+1;
    }
}
