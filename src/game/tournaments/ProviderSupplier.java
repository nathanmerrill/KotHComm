package game.tournaments;

import game.GamePlayer;

public interface ProviderSupplier<T extends GamePlayer> {
    GameProvider<T> getTournament();
}
