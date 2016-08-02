package KoTHComm.game.tournaments;

import KoTHComm.game.AbstractPlayer;

public interface Tournament<T extends AbstractPlayer<T>> extends GameProvider<T>, GameRanker<T>{

}
