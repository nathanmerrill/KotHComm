package game.tournaments;

import game.AbstractPlayer;

public interface ProviderSupplier<T extends AbstractPlayer<T>> {
    GameProvider<T> getTournament();
}
