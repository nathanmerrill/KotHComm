package game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public final class PlayerType {
    private final Supplier<? extends Player> supplier;
    private final String name;

    public PlayerType(String name, Supplier<? extends Player> supplier){
        this.supplier = supplier;
        this.name = name;
    }

    public PlayerType(Class<? extends Player> clazz){
        this(clazz.getSimpleName(), getSupplier(clazz));
    }

    private static Supplier<? extends Player> getSupplier(Class<? extends Player> player){
        Constructor<? extends Player> constructor;
        try {
            constructor =  player.getConstructor();
        } catch (NoSuchMethodException e){
            throw new RuntimeException(player.getName()+" needs a no-argument constructor", e);
        }
        constructor.setAccessible(true);
        return () -> create(constructor);
    }

    private static Player create(Constructor<? extends Player> constructor){
        try {
            return constructor.newInstance();
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException e){
            throw new RuntimeException("Cannot create player", e);
        }
    }

    public Player create(){
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
