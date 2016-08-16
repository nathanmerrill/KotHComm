package KoTHComm.game.tournaments;

import KoTHComm.game.*;

import java.util.function.Supplier;

public interface GameProvider<T extends AbstractPlayer<T>> extends Supplier<AbstractGame<T>>{

    

}