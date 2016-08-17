package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.AbstractPlayer;

public interface ProviderSupplier<T extends AbstractPlayer<T>> {
    GameProvider<T> getTournament();
}
