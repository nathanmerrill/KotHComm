package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.*;
import com.ppcg.kothcomm.game.scoreboards.Scoreboard;

import java.util.function.Supplier;

public interface GameProvider<T extends AbstractPlayer<T>>{
    AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard);
}