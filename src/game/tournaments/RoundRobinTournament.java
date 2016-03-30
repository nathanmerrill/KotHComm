package game.tournaments;

import game.*;
import utils.iterables.CombinationIterable;
import java.util.*;
import java.util.function.Function;


public final class RoundRobinTournament implements Tournament{

    private final GameManager gameManager;
    private final List<PlayerType> players;
    private final Scoreboard scoreboard;
    public RoundRobinTournament(GameManager gameManager, Scoreboard scoreboard, List<PlayerType> players){
        this.gameManager = gameManager;
        this.players = players;
        this.scoreboard = scoreboard;
    }

    @Override
    public PlayerRanking run() {
        List<List<PlayerType>> playerSets = new ArrayList<>();
        new CombinationIterable<>(players, gameManager.preferredPlayerCount()).forEach(playerSets::add);
        gameManager.runGames(playerSets).forEach(scoreboard::addScores);
        return scoreboard.playerRanking();
    }
}
