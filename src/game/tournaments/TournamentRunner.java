package game.tournaments;

import game.*;
import game.tournaments.types.SamplingProvider;
import game.tournaments.types.ScoreboardRanker;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TournamentRunner<T extends GamePlayer> {
    private final ProviderSupplier<T> providerSupplier;
    private final RankerSupplier<T> rankerSupplier;
    private final TournamentSupplier<T> bothSupplier;
    public TournamentRunner(ProviderSupplier<T> providerSupplier, RankerSupplier<T> rankerSupplier){
        this.providerSupplier = providerSupplier;
        this.rankerSupplier = rankerSupplier;
        bothSupplier = null;
    }

    public TournamentRunner(TournamentSupplier<T> bothSupplier){
        this.bothSupplier = bothSupplier;
        this.providerSupplier = null;
        this.rankerSupplier = null;
    }

    public TournamentRunner(GameManager<T> gameManager){
        this(new SamplingProvider<>(gameManager), new ScoreboardRanker<>(gameManager));
    }



    @SuppressWarnings("ConstantConditions")
    public Scoreboard<PlayerType<T>> run(int numGames){
        GameProvider<T> provider;
        GameRanker<T> ranker;
        if (bothSupplier == null){
            provider = providerSupplier.getTournament();
            ranker = rankerSupplier.getRanker();
        } else {
            Tournament<T> tournament = bothSupplier.get();
            provider = tournament;
            ranker = tournament;
        }

        Stream.generate(provider).limit(numGames).forEach(game -> {
            game.run();
            ranker.scoreGame(game);
        });

        return ranker.getRankings();
    }

}
