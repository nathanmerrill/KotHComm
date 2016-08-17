package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.*;

import java.util.function.Supplier;

public interface GameProvider<T extends AbstractPlayer<T>> extends Supplier<AbstractGame<T>>{

    

}