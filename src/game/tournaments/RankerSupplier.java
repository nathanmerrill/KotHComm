package game.tournaments;

import game.AbstractPlayer;

public interface RankerSupplier<T extends AbstractPlayer<T>>{
    GameRanker<T> getRanker();
}
