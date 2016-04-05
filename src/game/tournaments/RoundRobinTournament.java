package game.tournaments;

import game.*;
import utils.iterables.CombinationIterable;
import java.util.*;
import java.util.function.Function;


public class RoundRobinTournament<T> implements Tournament<T>{

    private final GameManager<T> gameManager;
    private final Scoreboard<T> scoreboard;
    public RoundRobinTournament(GameManager<T> gameManager, Scoreboard<T> scoreboard){
        this.gameManager = gameManager;
        this.scoreboard = scoreboard;
    }

    @Override
    public Scoreboard<T> run() {
        scoreboard.clear();
        List<List<PlayerType<T>>> playerSets = new ArrayList<>();
        new CombinationIterable<>(gameManager.getDirectory().allPlayers(), gameManager.preferredPlayerCount()).forEach(playerSets::add);
        gameManager.runGames(playerSets).forEach(scoreboard::addScores);
        return scoreboard;
    }
}
