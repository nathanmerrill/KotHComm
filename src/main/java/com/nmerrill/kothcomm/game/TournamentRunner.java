package com.nmerrill.kothcomm.game;

import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.game.players.Submission;
import com.nmerrill.kothcomm.game.scoring.Aggregator;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.game.tournaments.Tournament;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.DoubleList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.util.Random;
import java.util.function.Supplier;


public final class TournamentRunner<T extends AbstractPlayer<T>, U extends AbstractGame<T>> {
    private final Tournament<Submission<T>> tournament;
    private final Aggregator<Scoreboard<Submission<T>>> gameAggregator;
    private final Function<DoubleList, Double> playerAggregator;
    private final MutableList<Scoreboard<Submission<T>>> scoreList;
    private final MutableSet<U> currentGames;
    private final Supplier<U> gameSupplier;
    private final int gameSize;
    private final Random random;
    private Scoreboard<Submission<T>> aggregate;

    public TournamentRunner(
            Tournament<Submission<T>> tournament,
            Aggregator<Scoreboard<Submission<T>>> gameAggregator,
            Function<DoubleList, Double> playerAggregator,
            int gameSize,
            Supplier<U> gameSupplier,
            Random random
    ) {
        this.tournament = tournament;
        this.gameAggregator = gameAggregator;
        this.playerAggregator = playerAggregator;
        this.scoreList = Lists.mutable.empty();
        this.aggregate = new Scoreboard<>();
        this.currentGames = Sets.mutable.empty();
        this.gameSupplier = gameSupplier;
        this.gameSize = gameSize;
        this.random = random;
    }

    public TournamentRunner(Tournament<Submission<T>> tournament,
                            Aggregator<Scoreboard<Submission<T>>> gameAggregator,
                            int gameSize,
                            Supplier<U> gameSupplier,
                            Random random) {
        this(tournament, gameAggregator, DoubleList::average, gameSize, gameSupplier, random);
    }

    public Scoreboard<Submission<T>> scoreboard() {
        resolveGames();
        return aggregate;
    }

    public U createGame() {
        resolveGames();
        U game = gameSupplier.get();
        MutableList<T> players = tournament.get(gameSize, aggregate).collect(Submission::create);
        players.forEachWith(AbstractPlayer::setRandom, random);
        game.addPlayers(players);
        game.setRandom(random);
        currentGames.add(game);
        return game;
    }

    public MutableList<Scoreboard<Submission<T>>> getScoreList() {
        resolveGames();
        return scoreList;
    }

    private void resolveGames() {
        MutableSet<U> finishedGames = currentGames.select(AbstractGame::finished);
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
