package game.tournaments;

import game.AbstractPlayer;

public interface Tournament<T extends AbstractPlayer<T>> extends GameProvider<T>, GameRanker<T>{

}
