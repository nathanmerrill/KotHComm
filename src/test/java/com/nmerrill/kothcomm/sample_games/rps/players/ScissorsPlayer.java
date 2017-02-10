package com.nmerrill.kothcomm.sample_games.rps.players;

import com.nmerrill.kothcomm.sample_games.rps.Choice;
import com.nmerrill.kothcomm.sample_games.rps.RPSPlayer;

public class ScissorsPlayer extends RPSPlayer{
    @Override
    public Choice makeChoice() {
        return Choice.SCISSORS;
    }
}
