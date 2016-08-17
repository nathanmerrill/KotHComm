package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.AbstractPlayer;

public interface RankerSupplier<T extends AbstractPlayer<T>>{
    GameRanker<T> getRanker();
}
