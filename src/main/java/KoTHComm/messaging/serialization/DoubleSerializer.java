package KoTHComm.messaging.serialization;

public class DoubleSerializer implements Serializer<Double>{

    @Override
    public Double deserialize(String representation) {
        return Double.parseDouble(representation);
    }

    @Override
    public String serialize(Double value) {
        return value.toString();
    }
}
