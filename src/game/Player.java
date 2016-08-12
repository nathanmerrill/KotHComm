package game;

import utils.Tools;

import java.util.List;

public abstract class Player extends AbstractPlayer<Player> {
    protected List<Stock> ownedStock;
    /**
     * @param offers All available offers
     * @return A map containing the quantity of offers you would like to take
     */
    public abstract Offer acceptOffer(List<Offer> offers);

    /**
     * Allows you to place an offer on the market.  Called once per round.
     * If you return null, no offer will be given.
     * @return The offer you would like to make this round
     */
    public abstract Offer makeOffer();

    /**
     * Informs you of the real value of a single stock.  Only called once at the beginning of the game
     * @param stock The stock number
     * @param price The cash-out price of the indicated stock
     */
    public abstract void stockValue(int stock, double price);

    public final void setOwnedStock(List<Stock> stock){
        ownedStock = stock;
    }

    protected int getOwnedStock(int stockType){
        return ownedStock.get(stockType).getAmount();
    }

    public Stock randomStock(){
        return Tools.sample(ownedStock, getRandom());
    }
}
