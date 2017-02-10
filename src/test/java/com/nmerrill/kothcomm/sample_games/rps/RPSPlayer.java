package com.nmerrill.kothcomm.sample_games.rps;

import com.nmerrill.kothcomm.game.players.AbstractPlayer;

public abstract class RPSPlayer extends AbstractPlayer<RPSPlayer>{
    public abstract Choice makeChoice();
}
