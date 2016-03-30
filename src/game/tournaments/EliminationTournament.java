package game.tournaments;

import game.*;
import utils.iterables.Pair;
import utils.iterables.Reversed;
import utils.iterables.Tools;
import utils.iterables.WithIndex;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public final class EliminationTournament implements Tournament {

    private final GameManager gameManager;
    private final List<PlayerType> players;
    private final Map<PlayerType, Double> previousScores;
    private final List<Set<PlayerType>> brackets;
    private final int winnerCount;

    public EliminationTournament(GameManager manager, List<PlayerType> players, int winnerCount, int numBrackets){
        this.gameManager = manager;
        this.players = players;
        this.winnerCount = winnerCount;
        previousScores = new HashMap<>();
        brackets = new ArrayList<>();
        for (int i = 0; i < numBrackets; i++){
            brackets.add(new HashSet<>());
        }
    }

    public EliminationTournament(GameManager manager, List<PlayerType> players){
        this(manager, players, Math.min(players.size(), manager.maxPlayerCount()), 1);
    }

    @Override
    public PlayerRanking run() {
        brackets.stream().forEach(Set::clear);
        previousScores.clear();
        previousScores.putAll(players.stream().collect(Collectors.toMap(Function.identity(), i -> 0.0)));
        brackets.get(0).addAll(players);
        PlayerRanking ranking = new PlayerRanking();
        while (brackets.stream().anyMatch(i -> i.size() > winnerCount)){
            for (Pair<Integer, Set<PlayerType>> bracket: new Reversed<>(new WithIndex<>(brackets))){
                int index = bracket.first();
                Set<PlayerType> players = bracket.second();
                ArrayList<PlayerType> sorted = new ArrayList<>(players);
                sorted.sort(Comparator.comparing(previousScores::get));
                List<List<PlayerType>> partitions = Tools.partition(sorted, gameManager.preferredPlayerCount());
                if (partitions.get(partitions.size()-1).size() < gameManager.minPlayerCount()){
                    partitions.remove(partitions.size()-1);
                }
                List<Scoreboard> scores = gameManager.runGames(partitions);
                List<PlayerType> losers = scores.stream()
                        .map(this::getLosers)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
                if (index == brackets.size()){
                    losers.forEach(ranking::rankTop);
                } else {
                    brackets.get(index+1).addAll(losers);
                }
            }
        }

        List<PlayerType> remainingPlayers = brackets.stream().flatMap(Collection::stream).collect(Collectors.toList());
        while (remainingPlayers.size() > winnerCount) {
            int bracketStart = Math.max(0, remainingPlayers.size()-gameManager.preferredPlayerCount());
            int bracketEnd = remainingPlayers.size();
            List<PlayerType> losers = getLosers(gameManager.runGame(remainingPlayers.subList(bracketStart, bracketEnd)));
            losers.forEach(ranking::rankTop);
            remainingPlayers.removeAll(losers);
        }
        remainingPlayers.forEach(ranking::rankTop);
        return ranking;
    }

    private List<PlayerType> getLosers(Scoreboard scoreboard){
        List<Pair<PlayerType, Double>> aggregates = scoreboard.playerAggregates();
        previousScores.putAll(Pair.toMap(aggregates));
        List<PlayerType> ranked = aggregates.stream().map(Pair::first).collect(Collectors.toList());
        return ranked.subList(winnerCount, ranked.size());
    }
}
