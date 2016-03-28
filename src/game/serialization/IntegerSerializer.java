package game.serialization;

public class IntegerSerializer implements Serializer<Integer> {
    @Override
    public String serialize(Integer value) {
        return value.toString();
    }

    @Override
    public Integer deserialize(String representation) {
        return Integer.parseInt(representation);
    }

    @Override
    public String define() {
        return "Integer";
    }

    @Override
    public int separationLevel() {
        return 0;
    }
}