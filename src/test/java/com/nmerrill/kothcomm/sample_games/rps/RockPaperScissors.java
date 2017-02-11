package com.nmerrill.kothcomm.sample_games.rps;

import com.nmerrill.kothcomm.game.KotHComm;
import com.nmerrill.kothcomm.sample_games.rps.players.PaperPlayer;
import com.nmerrill.kothcomm.sample_games.rps.players.RandomPlayer;
import com.nmerrill.kothcomm.sample_games.rps.players.RockPlayer;
import com.nmerrill.kothcomm.sample_games.rps.players.ScissorsPlayer;
import com.nmerrill.kothcomm.ui.text.TextUI;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class RockPaperScissors {

    @Test
    public void run() {
        KotHComm<RPSPlayer, RPSGame> kotHComm = new KotHComm<>(RPSGame::new);
        kotHComm.addSubmission("Rock", RockPlayer::new);
        kotHComm.addSubmission("Paper", PaperPlayer::new);
        kotHComm.addSubmission("Scissors", ScissorsPlayer::new);
        kotHComm.addSubmission("Random", RandomPlayer::new);
        kotHComm.setGameSize(4);
        kotHComm.setPrinter(new TextUI(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {}
        })));
        kotHComm.run(new String[]{});
    }
}
