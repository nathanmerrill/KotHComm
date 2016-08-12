package game.bots;

import game.Offer;
import game.Player;

import java.util.List;

public class DumbBot extends Player {
    @Override
    public Offer acceptOffer(List<Offer> offers) {
        int max = 0;
        Offer maxOffer = null;
        for (Offer offer: offers){
            int difference = offer.getOffer().getAmount()-offer.getPayment().getAmount();
            if (difference > max){
                max = difference;
                maxOffer = offer;
            }
        }
        return maxOffer;
    }

    @Override
    public void stockValue(int stock, double price) {

    }

    @Override
    public Offer makeOffer() {
        return new Offer(
            randomStock().setAmount(getRandom().nextInt(100)),
            randomStock().setAmount(getRandom().nextInt(100))
        );
    }
}
