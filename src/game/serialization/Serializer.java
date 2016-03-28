package game.serialization;

public interface Serializer<T>{
    String serialize(T value);
    T deserialize(String representation);
    int separationLevel();
    String define();
}