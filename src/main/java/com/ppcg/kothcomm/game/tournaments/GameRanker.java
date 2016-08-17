package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.*;

public interface GameRanker<T extends AbstractPlayer<T>> {
    void scoreGame(AbstractGame<T> game);
    Scoreboard<PlayerType<T>> getRankings();
}
