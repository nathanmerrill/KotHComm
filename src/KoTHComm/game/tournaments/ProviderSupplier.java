package KoTHComm.game.tournaments;

import KoTHComm.game.AbstractPlayer;

public interface ProviderSupplier<T extends AbstractPlayer<T>> {
    GameProvider<T> getTournament();
}
