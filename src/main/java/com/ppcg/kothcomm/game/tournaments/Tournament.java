package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.AbstractPlayer;

public interface Tournament<T extends AbstractPlayer<T>> extends GameProvider<T>, GameRanker<T>{

}
