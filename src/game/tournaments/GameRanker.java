package game.tournaments;

import game.*;

public interface GameRanker<T extends GamePlayer> {
    void scoreGame(AbstractGame<T> game);
    Scoreboard<PlayerType<T>> getRankings();
}
