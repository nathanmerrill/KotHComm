package game;

import utils.Tools;

public final class Stock {
    private final int stockType, amount;
    public Stock(int stockType, int amount){
        assert(Tools.inRange(stockType, 0, StockExchange.NUM_STOCKS));
        assert(amount > 0);
        this.stockType = stockType;
        this.amount = amount;
    }

    public int getType() {
        return stockType;
    }

    public int getAmount() {
        return amount;
    }

    public Stock minus(Stock other){
        assert(other.stockType == this.stockType);
        return new Stock(stockType, amount-other.amount);
    }

    public Stock plus(Stock other){
        assert(other.stockType == this.stockType);
        return new Stock(stockType, amount+other.amount);
    }

    public Stock minus(int amount){
        return new Stock(stockType, this.amount - amount);
    }

    public Stock plus(int amount){
        return new Stock(stockType, this.amount + amount);
    }

    public Stock setAmount(int amount){
        return new Stock(stockType, amount);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;

        if (stockType != stock.stockType) return false;
        if (amount != stock.amount) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockType;
        result = 31 * result + amount;
        return result;
    }
}
