package game;

import messaging.serialization.Serializer;

public class StockSerializer implements Serializer<Stock>{
    public final static String SEPARATOR = ":";
    @Override
    public Stock deserialize(String representation) {
        String[] parts = representation.split(SEPARATOR);
        return new Stock(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    @Override
    public String serialize(Stock value) {
        return value.getType()+SEPARATOR+value.getAmount();
    }
}
