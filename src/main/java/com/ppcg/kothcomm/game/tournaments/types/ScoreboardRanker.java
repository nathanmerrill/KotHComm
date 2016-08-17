package com.ppcg.kothcomm.game.tournaments.types;

import com.ppcg.kothcomm.game.*;
import com.ppcg.kothcomm.game.tournaments.GameRanker;
import com.ppcg.kothcomm.game.tournaments.RankerSupplier;
import com.ppcg.kothcomm.utils.Pair;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ScoreboardRanker<T extends AbstractPlayer<T>> implements RankerSupplier<T> {
    private final Supplier<Scoreboard<PlayerType<T>>> supplier;
    public ScoreboardRanker(Supplier<Scoreboard<PlayerType<T>>> supplier){
        this.supplier = supplier;
    }

    public ScoreboardRanker(){
        this(Scoreboard::new);
    }

    @Override
    public ScoreboardScorer getRanker() {
        return new ScoreboardScorer();
    }
    public class ScoreboardScorer implements GameRanker<T> {
        private final Scoreboard<PlayerType<T>> scoreboard;
        public ScoreboardScorer(){
            scoreboard = supplier.get();
        }
        @Override
        public Scoreboard<PlayerType<T>> getRankings() {
            return scoreboard;
        }

        @Override
        public void scoreGame(AbstractGame<T> game) {
            game.onFinish(this::onFinish);
        }

        private void onFinish(Scoreboard<T> gameScores){
            scoreboard.addScores(gameScores.stream()
                    .map(Pair.fromValue(
                        AbstractPlayer::getType,
                        gameScores::getScore))
                    .collect(Collectors.toList()));
        }
    }
}
