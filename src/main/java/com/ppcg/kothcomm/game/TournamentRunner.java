package com.ppcg.kothcomm.game;

import com.ppcg.kothcomm.game.scoreboards.AggregateScoreboard;
import com.ppcg.kothcomm.game.scoreboards.Scoreboard;
import com.ppcg.kothcomm.game.tournaments.GameProvider;
import com.ppcg.kothcomm.game.tournaments.SamplingProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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

    public Scoreboard<PlayerType<T>> run(int numGames){
        return run(numGames, new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        }));
    }

    @SuppressWarnings("ConstantConditions")
    public Scoreboard<PlayerType<T>> run(int numGames, PrintStream stream){
        stream.println("Running "+numGames+" games");
        GameProvider<T> provider = providerSupplier.get();
        Scoreboard<PlayerType<T>> scoreboard = rankerSupplier.get();
        int updateFrequency = Math.max(1, numGames/100);
        for (int i = 0; i < numGames; i++){
            if (i%updateFrequency == 0){
                stream.println("Game "+i);
            }
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
