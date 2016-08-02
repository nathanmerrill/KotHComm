package KoTHComm.game.tournaments.types;

import KoTHComm.game.*;
import KoTHComm.game.tournaments.GameRanker;
import KoTHComm.game.tournaments.RankerSupplier;
import KoTHComm.utils.Pair;

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
