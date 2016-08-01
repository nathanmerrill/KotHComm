package game.tournaments;

import game.GamePlayer;

import java.util.function.Supplier;

public interface TournamentSupplier<T extends GamePlayer> extends Supplier<Tournament<T>>, ProviderSupplier<T>, RankerSupplier<T>{
    @Override
    default GameRanker<T> getRanker(){
        return get();
    }

    @Override
    default GameProvider<T> getTournament(){
        return get();
    }
}
