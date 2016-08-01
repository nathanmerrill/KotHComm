package game.tournaments;

import game.*;

public interface GameRanker<T extends AbstractPlayer<T>> {
    void scoreGame(AbstractGame<T> game);
    Scoreboard<PlayerType<T>> getRankings();
}
