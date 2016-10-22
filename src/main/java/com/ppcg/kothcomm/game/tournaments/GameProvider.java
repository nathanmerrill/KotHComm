package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.*;
import com.ppcg.kothcomm.game.scoring.Scoreboard;

public interface GameProvider<T extends AbstractPlayer<T>>{
    AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard);
}