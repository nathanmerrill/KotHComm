package game.tournaments;

import game.*;
import utils.iterables.Pair;
import utils.iterables.Reversed;
import utils.iterables.Tools;
import utils.iterables.WithIndex;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public final class EliminationTournament<T> implements Tournament<T> {

    private final GameManager<T> gameManager;
    private final Directory<T> directory;
    private final Map<PlayerType<T>, Double> previousScores;
    private final List<Set<PlayerType<T>>> brackets;
    private final int winnerCount;

    public EliminationTournament(GameManager<T> manager, int winnerCount, int numBrackets){
        this.gameManager = manager;
        this.directory = gameManager.getDirectory();
        this.winnerCount = winnerCount;
        previousScores = new HashMap<>();
        brackets = new ArrayList<>();
        for (int i = 0; i < numBrackets; i++){
            brackets.add(new HashSet<>());
        }
    }

    public EliminationTournament(GameManager<T> manager){
        this(manager, Math.min(manager.getDirectory().playerCount(), manager.maxPlayerCount()), 1);
    }

    @Override
    public PlayerRanking<T> run() {
        brackets.stream().forEach(Set::clear);
        previousScores.clear();
        previousScores.putAll(directory.allPlayers().stream().collect(Collectors.toMap(Function.identity(), i -> 0.0)));
        brackets.get(0).addAll(directory.allPlayers());
        PlayerRanking<T> ranking = new PlayerRanking<>();
        while (brackets.stream().anyMatch(i -> i.size() > winnerCount)){
            for (Pair<Integer, Set<PlayerType<T>>> bracket: new Reversed<>(new WithIndex<>(brackets))){
                int index = bracket.first();
                Set<PlayerType<T>> players = bracket.second();
                ArrayList<PlayerType<T>> sorted = new ArrayList<>(players);
                sorted.sort(Comparator.comparing(previousScores::get));
                List<List<PlayerType<T>>> partitions = Tools.partition(sorted, gameManager.preferredPlayerCount());
                if (partitions.get(partitions.size()-1).size() < gameManager.minPlayerCount()){
                    partitions.remove(partitions.size()-1);
                }
                List<Scoreboard<T>> scores = gameManager.runGames(partitions);
                List<PlayerType<T>> losers = scores.stream()
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

        List<PlayerType<T>> remainingPlayers = brackets.stream().flatMap(Collection::stream).collect(Collectors.toList());
        while (remainingPlayers.size() > winnerCount) {
            int bracketStart = Math.max(0, remainingPlayers.size()-gameManager.preferredPlayerCount());
            int bracketEnd = remainingPlayers.size();
            List<PlayerType<T>> losers = getLosers(gameManager.runGame(remainingPlayers.subList(bracketStart, bracketEnd)));
            losers.forEach(ranking::rankTop);
            remainingPlayers.removeAll(losers);
        }
        remainingPlayers.forEach(ranking::rankTop);
        return ranking;
    }

    private List<PlayerType<T>> getLosers(Scoreboard<T> scoreboard){
        List<Pair<PlayerType<T>, Double>> aggregates = scoreboard.playerAggregates();
        previousScores.putAll(Pair.toMap(aggregates));
        List<PlayerType<T>> ranked = aggregates.stream().map(Pair::first).collect(Collectors.toList());
        return ranked.subList(winnerCount, ranked.size());
    }
}
