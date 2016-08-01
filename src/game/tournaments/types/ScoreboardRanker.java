package game.tournaments.types;

import game.*;
import game.tournaments.GameRanker;
import game.tournaments.RankerSupplier;
import utils.Pair;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ScoreboardRanker<T extends GamePlayer> implements RankerSupplier<T> {
    private final Supplier<Scoreboard<PlayerType<T>>> supplier;
    private final GameManager<T> manager;
    public ScoreboardRanker(Supplier<Scoreboard<PlayerType<T>>> supplier, GameManager<T> manager){
        this.supplier = supplier;
        this.manager = manager;
    }

    public ScoreboardRanker(GameManager<T> manager){
        this(Scoreboard::new, manager);
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
                        manager.getDirectory()::getType,
                        gameScores::getScore))
                    .collect(Collectors.toList()));
        }
    }
}
