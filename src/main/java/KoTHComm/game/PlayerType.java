package KoTHComm.game;

import java.util.function.Supplier;

public final class PlayerType<T extends AbstractPlayer<T>> {
    private final Supplier<T> supplier;
    private final String name;

    public PlayerType(String name, Supplier<T> supplier){
        this.supplier = supplier;
        this.name = name;
    }

    public T create(){
        T player = supplier.get();
        player.setType(this);
        return player;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerType that = (PlayerType) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
