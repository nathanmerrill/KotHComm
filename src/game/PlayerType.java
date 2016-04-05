package game;

import java.util.function.Supplier;

public class PlayerType<T> {
    private final Supplier<T> supplier;
    private final String name;

    public PlayerType(String name, Supplier<T> supplier){
        this.supplier = supplier;
        this.name = name;
    }

    public T create(){
        return supplier.get();
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
}
