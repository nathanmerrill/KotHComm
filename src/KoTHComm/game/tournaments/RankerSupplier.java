package KoTHComm.game.tournaments;

import KoTHComm.game.AbstractPlayer;

public interface RankerSupplier<T extends AbstractPlayer<T>>{
    GameRanker<T> getRanker();
}
