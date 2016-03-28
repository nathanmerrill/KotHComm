package game.tournaments;

import game.*;
import utils.iterables.Tools;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public final class SamplingTournament implements Tournament {

    private final GameManager gameManager;
    private final List<Player> players;
    private final List<Player> currentPopulation;
    private final Scoreboard scoreboard;

    public SamplingTournament(GameManager gameManager,
                              List<Player> players,
                              Scoreboard scoreboard){
        this.gameManager = gameManager;
        this.players = players;
        this.currentPopulation = new ArrayList<>();
        this.scoreboard = scoreboard;
    }


    @Override
    public PlayerRanking run() {
        currentPopulation.clear();
        currentPopulation.addAll(players);
        scoreboard.clear();
        Collections.shuffle(currentPopulation);
        List<List<Player>> playerSets = new ArrayList<>();
        while (true) {
            List<List<Player>> partitions = Tools.partition(currentPopulation, gameManager.preferredPlayerCount());
            if (partitions.get(partitions.size()-1).size() == gameManager.preferredPlayerCount()){
                playerSets.addAll(partitions);
                break;
            }
            Set<Player> remaining = new HashSet<>(partitions.remove(partitions.size()-1));
            List<Player> availablePlayers;
            if (!gameManager.allowDuplicates()) {
                availablePlayers = players.stream().filter(i -> !remaining.contains(i)).collect(Collectors.toList());
            } else {
                availablePlayers = new ArrayList<>(players);
            }
            Collections.shuffle(availablePlayers);
            remaining.addAll(availablePlayers.subList(0, gameManager.preferredPlayerCount() - remaining.size()));
            playerSets.add(new ArrayList<>(remaining));
            currentPopulation.clear();
            currentPopulation.addAll(players.stream().filter(i -> !remaining.contains(i)).collect(Collectors.toList()));
        }
        gameManager.runGames(playerSets).forEach(scoreboard::addScores);
        return scoreboard.playerRanking();
    }


}
