package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;

public interface Tournament<T extends AbstractPlayer<T>>{
    AbstractGame<T> get(Scoreboard<PlayerType<T>> ranking);
}