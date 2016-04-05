package game.tournaments;

import game.*;
import utils.iterables.Tools;

import java.util.*;
import java.util.stream.Collectors;


public class SamplingTournament<T> implements Tournament<T> {

    private final GameManager<T> gameManager;
    private final Directory<T> directory;
    private final List<PlayerType<T>> currentPopulation;
    private final Scoreboard<T> scoreboard;

    public SamplingTournament(GameManager<T> gameManager,
                              Scoreboard<T> scoreboard){
        this.gameManager = gameManager;
        this.directory = gameManager.getDirectory();
        this.currentPopulation = new ArrayList<>();
        this.scoreboard = scoreboard;
    }


    @Override
    public Scoreboard<T> run() {
        currentPopulation.clear();
        currentPopulation.addAll(directory.allPlayers());
        scoreboard.clear();
        Collections.shuffle(currentPopulation);
        List<List<PlayerType<T>>> playerSets = new ArrayList<>();
        while (true) {
            List<List<PlayerType<T>>> partitions = Tools.partition(currentPopulation, gameManager.preferredPlayerCount());
            if (partitions.get(partitions.size()-1).size() == gameManager.preferredPlayerCount()){
                playerSets.addAll(partitions);
                break;
            }
            Set<PlayerType<T>> remaining = new HashSet<>(partitions.remove(partitions.size()-1));
            List<PlayerType<T>> availablePlayers;
            if (!gameManager.allowsDuplicates()) {
                availablePlayers = directory.allPlayers().stream().filter(i -> !remaining.contains(i)).collect(Collectors.toList());
            } else {
                availablePlayers = directory.allPlayers();
            }
            Collections.shuffle(availablePlayers);
            remaining.addAll(availablePlayers.subList(0, gameManager.preferredPlayerCount() - remaining.size()));
            playerSets.add(new ArrayList<>(remaining));
            currentPopulation.clear();
            currentPopulation.addAll(directory.allPlayers().stream().filter(i -> !remaining.contains(i)).collect(Collectors.toList()));
        }
        gameManager.runGames(playerSets).forEach(scoreboard::addScores);
        return scoreboard;
    }


}
