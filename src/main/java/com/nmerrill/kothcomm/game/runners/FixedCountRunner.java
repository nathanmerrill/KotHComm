package com.nmerrill.kothcomm.game.runners;

import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.MathTools;
import com.nmerrill.kothcomm.utils.NullStream;
import com.nmerrill.kothcomm.game.AbstractPlayer;

import java.io.PrintStream;


public final class FixedCountRunner<T extends AbstractPlayer<T>> {
    private final TournamentRunner<T> runner;

    public FixedCountRunner(TournamentRunner<T> runner){
        this.runner = runner;
    }

    public Scoreboard<PlayerType<T>> run(int numGames){
        return run(numGames, new NullStream());
    }

    public Scoreboard<PlayerType<T>> run(int numGames, PrintStream stream) {
        int updateFrequency = MathTools.clamp(numGames / 100, 1, 100);
        for (int i = 0; i < numGames; i++) {
            if (i % updateFrequency == 0) {
                stream.println("Game " + i);
            }
            runner.createGame().run();
        }
        Scoreboard<PlayerType<T>> scoreboard = runner.scoreboard();
        stream.println(scoreboard.scoreTable());
        return scoreboard;
    }

}
