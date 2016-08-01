package game.tournaments;

import game.GamePlayer;

public interface RankerSupplier<T extends GamePlayer>{
    GameRanker<T> getRanker();
}
