package com.ppcg.kothcomm.game;

import com.ppcg.kothcomm.game.scoring.Aggregator;
import com.ppcg.kothcomm.game.scoring.ItemAggregator;
import com.ppcg.kothcomm.game.scoring.Scoreboard;
import com.ppcg.kothcomm.game.tournaments.GameProvider;
import com.ppcg.kothcomm.game.tournaments.SamplingProvider;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.DoubleList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.primitive.DoubleLists;

import java.util.function.Supplier;

public class GameRunner<T extends AbstractPlayer<T>> {
    private final Supplier<GameProvider<T>> providerSupplier;
    private final Aggregator<Scoreboard<PlayerType<T>>> gameAggregator;
    private final Function<DoubleList, Double> playerAggregator;

    public GameRunner(
            Supplier<GameProvider<T>> providerSupplier,
            Aggregator<Scoreboard<PlayerType<T>>> gameAggregator,
            Function<DoubleList, Double> playerAggregator
    ){
        this.providerSupplier = providerSupplier;
        this.gameAggregator = gameAggregator;
        this.playerAggregator = playerAggregator;
    }

    public GameRunner(Supplier<GameProvider<T>> providerSupplier,
                      Aggregator<Scoreboard<PlayerType<T>>> gameAggregator){
        this(providerSupplier, gameAggregator, DoubleList::average);
    }

    public GameRunner(GameManager<T> gameManager){
        this(new SamplingProvider<>(gameManager), new ItemAggregator<>(), DoubleList::average);
    }

    public Scoreboard<PlayerType<T>> run(int numGames){
        GameProvider<T> provider = providerSupplier.get();
        MutableList<Scoreboard<PlayerType<T>>> scoreList = Lists.mutable.empty();
        for (int i = 0; i < numGames; i++){
            Scoreboard<T> results = provider.get(gameAggregator.aggregate(scoreList)).run();
            if (results.isEmpty()){
                continue;
            }

            Scoreboard<PlayerType<T>> scoreboard = new Scoreboard<>();
            results.scores()
                    .keyValuesView()
                    .aggregateInPlaceBy(p -> p.getOne().getType(), DoubleLists.mutable::empty, (l, p) -> l.add(p.getTwo()))
                    .forEachKeyValue((j,l) -> scoreboard.addScore(j, playerAggregator.valueOf(l)));
            scoreList.add(scoreboard);
        }
        return gameAggregator.aggregate(scoreList);
    }


}
