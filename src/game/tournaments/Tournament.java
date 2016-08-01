package game.tournaments;

import game.GamePlayer;

public interface Tournament<T extends GamePlayer> extends GameProvider<T>, GameRanker<T>{

}
