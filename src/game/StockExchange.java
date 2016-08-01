package game;

import utils.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class StockExchange extends RepeatedGame<Player> {
    public static final int INITIAL_STOCK_QUANTITY = 1000;
    public static final int NUM_EXCHANGES = 1000;
    public static final int NUM_STOCKS = 5;

    private List<Double> prices;

    private HashMap<Player, List<Stock>> stockMarket;

    public StockExchange(){
        super(NUM_EXCHANGES);
    }

    @Override
    public void setup() {
        initPrices();
        initStockMarket();
        givePlayersPrivateInfo();
    }

    private void initPrices(){
        prices = random.doubles().limit(NUM_STOCKS).mapToObj(i->i).collect(Collectors.toList());
    }

    private void initStockMarket(){
        stockMarket = new HashMap<>();
        List<Stock> initialStock = new ArrayList<>();
        for (int i = 0; i< NUM_STOCKS; i++){
            initialStock.add(new Stock(i, INITIAL_STOCK_QUANTITY));
        }
        players.forEach(p -> stockMarket.put(p, new ArrayList<>(initialStock)));
    }

    private void givePlayersPrivateInfo(){
        for (int i = 0; i < NUM_STOCKS; i++){
            players.get(i).stockValue(i, prices.get(i));
        }
    }

    @Override
    public Scoreboard<Player> getScores() {
        Scoreboard<Player> scoreboard = new Scoreboard<>();
        players.forEach(p -> scoreboard.addScore(p, getNetWorth(p)));
        return scoreboard;
    }

    private double getNetWorth(Player player){
        return stockMarket.get(player).stream().mapToDouble(this::stockValue).sum();
    }

    private double stockValue(Stock stock){
        return prices.get(stock.getType())*stock.getAmount();
    }

    private void updatePlayersStocks(){
        players.forEach(player -> player.setOwnedStock(new ArrayList<>(stockMarket.get(player))));
    }

    private List<Pair<Player, Offer>> getPlayerOffers(){
        return players.stream()
                .map(Pair.fromValue(Player::makeOffer))
                .filter(p -> canPay(p.first(), p.second().getOffer()))
                .collect(Collectors.toList());
    }

    private List<Pair<Player, Offer>> getAvailableOffersFor(Player player, List<Pair<Player, Offer>> currentOffers){
        return currentOffers.stream()
                .filter(i -> canPay(player, i.second().getPayment()))
                .collect(Collectors.toList());
    }

    private boolean canPay(Player player, Stock stock){
        return stock.getAmount() <= stockMarket.get(player).get(stock.getType()).getAmount();
    }

    private Pair<Player, Offer> giveOffers(Player player, List<Pair<Player, Offer>> availableOffers){
        Offer accepted = player.acceptOffer(availableOffers.stream().map(Pair::second).collect(Collectors.toList()));
        if (accepted == null){
            return null;
        }
        for (Pair<Player, Offer> available: availableOffers){
            if (accepted.equals(available.second())){
                return available;
            }
        }
        System.out.println(player.getName()+" tried to accept an invalid offer");
        return null;
    }

    @Override
    protected void nextStep() {
        updatePlayersStocks();
        List<Pair<Player, Offer>> currentOffers = getPlayerOffers();

        for (Player player: players){
            List<Pair<Player, Offer>> availableOffers = getAvailableOffersFor(player, currentOffers);
            Pair<Player, Offer> accepted = giveOffers(player, availableOffers);
            if (accepted == null){
                continue;
            }
            currentOffers.remove(accepted);
            exchange(accepted.second(), accepted.first(), player);
        }
    }

    private void exchange(Offer offer, Player offerer, Player accepter){
        if (!canPay(offerer, offer.getOffer()) || !canPay(accepter, offer.getPayment())){
            return;
        }
        removeStock(accepter, offer.getPayment());
        addStock(offerer, offer.getPayment());

        removeStock(offerer, offer.getOffer());
        addStock(accepter, offer.getOffer());
    }

    private void addStock(Player player, Stock stock){
        List<Stock> playerMarket = stockMarket.get(player);
        int stockType = stock.getType();
        playerMarket.set(stockType, playerMarket.get(stockType).plus(stock));
    }

    private void removeStock(Player player, Stock stock){
        List<Stock> playerMarket = stockMarket.get(player);
        int stockType = stock.getType();
        playerMarket.set(stockType, playerMarket.get(stockType).minus(stock));
    }

}

