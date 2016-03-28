package game.serialization;

public class StringSerializer implements Serializer<String>{
    @Override
    public String deserialize(String representation) {
        return representation;
    }

    @Override
    public String serialize(String value) {
        return value;
    }

    @Override
    public int separationLevel() {
        return 0;
    }

    @Override
    public String define() {
        return "String";
    }
}