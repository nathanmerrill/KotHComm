package com.nmerrill.kothcomm.game.runners;

import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.ui.text.TextUI;


public final class FixedCountRunner<T extends AbstractPlayer<T>> {
    private final TournamentRunner<T> runner;

    public FixedCountRunner(TournamentRunner<T> runner){
        this.runner = runner;
    }

    public Scoreboard<PlayerType<T>> run(int numGames){
        return run(numGames, new TextUI());
    }

    public Scoreboard<PlayerType<T>> run(int numGames, TextUI printer) {
        printer.out.println("Running "+numGames+" games");
        for (int i = 0; i < numGames; i++) {
            printer.printProgress(i, numGames);
            runner.createGame().run();
        }
        printer.printProgress(numGames, numGames);
        Scoreboard<PlayerType<T>> scoreboard = runner.scoreboard();
        printer.printScoreboard(scoreboard);
        return scoreboard;
    }

}
