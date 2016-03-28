package game.tournaments;

import game.*;
import utils.iterables.Pair;
import utils.iterables.PermutationIterable;
import utils.iterables.Tools;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class EloTournament implements Tournament{

    public static final double INITIAL_RATING = 1000;
    public static final double DEFAULT_KFACTOR = 32;

    private final double kFactor;
    private final GameManager gameManager;
    private final Map<Player, Double> ratings;
    private final int numGames;

    public EloTournament(GameManager gameManager, List<Player> players, int numGames, double kFactor){
        this.gameManager = gameManager;
        this.numGames = numGames;
        ratings = players.stream().collect(Collectors.toMap(Function.identity(), i -> INITIAL_RATING));
        this.kFactor = kFactor;
    }

    public EloTournament(GameManager gameManager, List<Player> players, int numGames){
        this(gameManager, players, numGames, DEFAULT_KFACTOR/players.size());
    }

    @Override
    public PlayerRanking run() {
        Iterator<Player> focuses = new ArrayList<Player>().iterator();
        for (int i = 0; i < numGames; i++){
            if (!focuses.hasNext()){
                focuses = Tools.apply(new ArrayList<>(ratings.keySet()), Collections::shuffle).iterator();
            }
            List<Player> players = rangeAround(focuses.next());
            Scoreboard scores = gameManager.runGame(players);
            updateScores(scores);
        }
        return new PlayerRanking(ratings.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()));
    }

    private void updateScores(Scoreboard scoreboard){
        Map<Player, Double> scores = mapScores(scoreboard);
        for (List<Player> pairing : new PermutationIterable<>(scores.keySet())){
            Pair<Double, Double> expected = expectedScore(new Pair<>(pairing.get(0), pairing.get(1)));
            BiConsumer<Player, Double> update = (p, d) -> ratings.put(p, ratings.get(p)+kFactor*(scores.get(p)-d));
            update.accept(pairing.get(0), expected.first());
            update.accept(pairing.get(1), expected.second());
        }
    }

    private Pair<Double, Double> expectedScore(Pair<Player, Player> matchup){
        double adj1 = Math.pow(10, ratings.get(matchup.first())/400);
        double adj2 = Math.pow(10, ratings.get(matchup.second())/400);
        double expected1 = adj1 / (adj1 + adj2);
        double expected2 = 1 - expected1;
        return new Pair<>(expected1, expected2);
    }

    private Map<Player, Double> mapScores(Scoreboard scores){
        List<Pair<Player, Double>> aggregates = scores.playerAggregates();
        double min = aggregates.get(0).second();
        double max = aggregates.get(aggregates.size()-1).second();
        double range = max - min;
        return aggregates.stream().collect(Collectors.toMap(Pair::first, i -> 1 - (i.second()-min)/range));
    }



    private List<Player> rangeAround(Player player){
        List<Player> sorted = ratings.keySet().stream()
                .sorted(Comparator.comparingDouble(ratings::get))
                .collect(Collectors.toList());
        int minIndex = sorted.indexOf(player);
        int maxIndex = minIndex;
        while (maxIndex - minIndex + 1 < gameManager.preferredPlayerCount()){
            if (minIndex == 0){
                maxIndex = gameManager.preferredPlayerCount();
                break;
            }
            if (maxIndex == sorted.size()-1){
                minIndex = sorted.size()-gameManager.preferredPlayerCount();
                break;
            }
            double minDiff = ratings.get(sorted.get(minIndex)) - ratings.get(sorted.get(minIndex-1));
            double maxDiff = ratings.get(sorted.get(maxIndex+1)) - ratings.get(sorted.get(maxIndex));
            if (maxDiff < minDiff){
                maxIndex++;
            } else {
                minIndex++;
            }
        }
        return sorted.subList(minIndex, maxIndex+1);
    }
}
