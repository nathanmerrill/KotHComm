package game.bots;

import game.*;
import messaging.PipeCommunicator;
import messaging.SerializedCommunicator;
import messaging.serialization.ListSerializer;

import java.util.List;

public class PipeBot extends Player{
    private final PipeCommunicator pipeCommunicator;
    private final SerializedCommunicator<List<Offer>, Offer> acceptCommunicator;
    private final SerializedCommunicator<List<Stock>, Offer> makeCommunicator;

    public PipeBot(String folderPath){
        pipeCommunicator = new PipeCommunicator(folderPath);
        acceptCommunicator = new SerializedCommunicator<>(pipeCommunicator, new ListSerializer<>(new OfferSerializer()), new OfferSerializer());
        makeCommunicator = new SerializedCommunicator<>(pipeCommunicator, new ListSerializer<>(new StockSerializer()), new OfferSerializer());
    }

    @Override
    public Offer acceptOffer(List<Offer> offers) {
        return acceptCommunicator.sendMessage(offers, "AcceptOffer");
    }

    @Override
    public void stockValue(int stock, double price) {
        pipeCommunicator.sendMessage(stock+":"+price, "StockValue");
    }

    @Override
    public Offer makeOffer() {
        return makeCommunicator.sendMessage(this.ownedStock, "MakeOffer");
    }
}
