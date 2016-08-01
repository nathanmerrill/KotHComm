package game;

public final class Offer {
    private final Stock offer, payment;
    public Offer(Stock offer, Stock payment){
        assert(offer.getType() != payment.getType());
        this.offer = offer;
        this.payment = payment;
    }

    public Stock getOffer() {
        return offer;
    }

    public Stock getPayment() {
        return payment;
    }

}
