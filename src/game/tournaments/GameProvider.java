package game.tournaments;

import game.*;

import java.util.function.Supplier;

public interface GameProvider<T extends AbstractPlayer<T>> extends Supplier<AbstractGame<T>>{

    

}