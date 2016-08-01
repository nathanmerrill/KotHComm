package game.tournaments;

import game.*;

import java.util.function.Supplier;

public interface GameProvider<T extends GamePlayer> extends Supplier<AbstractGame<T>>{

    

}