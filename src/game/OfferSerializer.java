package game;

import messaging.serialization.Serializer;

public class OfferSerializer implements Serializer<Offer>{
    public final static String SEPARATOR = ",";
    private final StockSerializer stock;
    public OfferSerializer(){
        stock = new StockSerializer();
    }
    @Override
    public String serialize(Offer value) {
        return stock.serialize(value.getOffer())+SEPARATOR+stock.serialize(value.getPayment());
    }

    @Override
    public Offer deserialize(String representation) {
        String[] parts = representation.split(SEPARATOR);
        return new Offer(stock.deserialize(parts[0]), stock.deserialize(parts[1]));
    }
}
