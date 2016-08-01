package game;

import java.util.Random;

public abstract class GamePlayer {

    private PlayerType type;
    private Random random;

    public final void setType(PlayerType type){
        this.type = type;
    }

    public final String getName(){
        return type.getName();
    }

    public final void setRandom(Random random){
        this.random = random;
    }

    public final Random getRandom(){
        return this.random;
    }

    @Override
    public final String toString() {
        return getName();
    }
}
