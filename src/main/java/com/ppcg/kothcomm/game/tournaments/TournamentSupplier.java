package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.AbstractPlayer;

import java.util.function.Supplier;

public interface TournamentSupplier<T extends AbstractPlayer<T>> extends Supplier<Tournament<T>>, ProviderSupplier<T>, RankerSupplier<T>{
    @Override
    default GameRanker<T> getRanker(){
        return get();
    }

    @Override
    default GameProvider<T> getTournament(){
        return get();
    }
}
