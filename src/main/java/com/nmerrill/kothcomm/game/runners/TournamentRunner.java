package com.nmerrill.kothcomm.game.runners;

import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.GameManager;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Aggregator;
import com.nmerrill.kothcomm.game.scoring.ItemAggregator;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.game.tournaments.Sampling;
import com.nmerrill.kothcomm.game.tournaments.Tournament;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.DoubleList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;


public final class TournamentRunner<T extends AbstractPlayer<T>> {
    private final Tournament<T> tournament;
    private final Aggregator<Scoreboard<PlayerType<T>>> gameAggregator;
    private final Function<DoubleList, Double> playerAggregator;
    private final MutableList<Scoreboard<PlayerType<T>>> scoreList;
    private final MutableSet<AbstractGame<T>> currentGames;
    private Scoreboard<PlayerType<T>> aggregate;

    public TournamentRunner(
            Tournament<T> tournament,
            Aggregator<Scoreboard<PlayerType<T>>> gameAggregator,
            Function<DoubleList, Double> playerAggregator
    ) {
        this.tournament = tournament;
        this.gameAggregator = gameAggregator;
        this.playerAggregator = playerAggregator;
        this.scoreList = Lists.mutable.empty();
        this.aggregate = new Scoreboard<>();
        this.currentGames = Sets.mutable.empty();
    }

    public TournamentRunner(Tournament<T> tournament,
                            Aggregator<Scoreboard<PlayerType<T>>> gameAggregator) {
        this(tournament, gameAggregator, DoubleList::average);
    }

    public TournamentRunner(GameManager<T> gameManager) {
        this(new Sampling<>(gameManager), new ItemAggregator<>(), DoubleList::average);
    }

    public Scoreboard<PlayerType<T>> scoreboard() {
        resolveGames();
        return aggregate;
    }

    public AbstractGame<T> createGame() {
        resolveGames();
        AbstractGame<T> game = tournament.get(aggregate);
        currentGames.add(game);
        return game;
    }

    public MutableList<Scoreboard<PlayerType<T>>> getScoreList() {
        resolveGames();
        return scoreList;
    }

    private void resolveGames() {
        MutableSet<AbstractGame<T>> finishedGames = currentGames.select(AbstractGame::finished);
        if (finishedGames.size() > 0) {
            scoreList.addAll(
                    finishedGames
                            .tap(currentGames::remove)
                            .collect(AbstractGame::getScores)
                            .collect(score -> score.mapScoreboard(AbstractPlayer::getType, playerAggregator))
            );
            aggregate = gameAggregator.aggregate(scoreList);
        }
    }


}
