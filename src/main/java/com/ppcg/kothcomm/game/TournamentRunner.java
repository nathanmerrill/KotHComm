package com.ppcg.kothcomm.game;

import com.ppcg.kothcomm.game.scoreboards.AggregateScoreboard;
import com.ppcg.kothcomm.game.scoreboards.Scoreboard;
import com.ppcg.kothcomm.game.tournaments.GameProvider;
import com.ppcg.kothcomm.game.tournaments.SamplingProvider;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TournamentRunner<T extends AbstractPlayer<T>> {
    private final Supplier<GameProvider<T>> providerSupplier;
    private final Supplier<Scoreboard<PlayerType<T>>> rankerSupplier;
    private final Function<List<Double>, Double> merger;
    public TournamentRunner(
            Supplier<GameProvider<T>> providerSupplier,
            Supplier<Scoreboard<PlayerType<T>>> rankerSupplier,
            Function<List<Double>, Double> merger
    ){
        this.providerSupplier = providerSupplier;
        this.rankerSupplier = rankerSupplier;
        this.merger = merger;
    }
    public TournamentRunner(
            Supplier<GameProvider<T>> providerSupplier,
            Supplier<Scoreboard<PlayerType<T>>> rankerSupplier
    ){
        this(providerSupplier, rankerSupplier, AggregateScoreboard::meanAggregator);
    }

    public TournamentRunner(GameManager<T> gameManager){
        this(new SamplingProvider<>(gameManager), AggregateScoreboard::new);
    }

    @SuppressWarnings("ConstantConditions")
    public Scoreboard<PlayerType<T>> run(int numGames){
        GameProvider<T> provider = providerSupplier.get();
        Scoreboard<PlayerType<T>> scoreboard = rankerSupplier.get();

        for (int i = 0; i < numGames; i++){

            Scoreboard<T> results = provider.get(scoreboard).run();
            if (results.isEmpty()){
                continue;
            }
            Map<PlayerType<T>, List<Double>> merged = new HashMap<>();
            for (T player: results){
                PlayerType<T> type = player.getType();
                merged.putIfAbsent(type, new ArrayList<>());
                merged.get(type).add(results.getScore(player));
            }
            Map<PlayerType<T>, Double> aggregated = merged.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, it -> merger.apply(it.getValue())));
            scoreboard.addScores(aggregated);
        }

        return scoreboard;
    }


}
