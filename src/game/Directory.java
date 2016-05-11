package game;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Directory<T> {
    private final Map<Class<?>, PlayerType<T>> registry;
    public Directory(){
        registry = new HashMap<>();
    }
    public Directory(List<T> players){
        this();
        players.forEach(i -> register(i.getClass()));
    }
    public List<PlayerType<T>> allPlayers(){
        return new ArrayList<>(registry.values());
    }
    public List<T> instantiate(Collection<PlayerType<T>> types){
        return types.stream().map(PlayerType::create).collect(Collectors.toList());
    }
    public List<T> instantiate(){
        return instantiate(registry.values());
    }
    public void register(Class<?> clazz, Supplier<T> supplier){
        registry.put(clazz, new PlayerType<>(clazz.getSimpleName(), supplier));
    }
    public void register(Class<?> clazz){
        register(clazz, getSupplier(clazz));
    }
    public int playerCount(){
        return registry.size();
    }

    public PlayerType<T> getType(Class<?> clazz){
        return registry.get(clazz);
    }

    public PlayerType<T> getType(T item){
        return registry.get(item.getClass());
    }

    private static <T> Supplier<T> getSupplier(Class<?> player){
        Constructor<?> constructor;
        try {
            constructor =  player.getConstructor();
        } catch (NoSuchMethodException e){
            throw new RuntimeException(player.getName()+" needs a no-argument constructor", e);
        }
        constructor.setAccessible(true);
        return () -> (T)create(constructor);
    }

    private static <T> T create(Constructor<T> constructor){
        try {
            return constructor.newInstance();
        } catch (InstantiationException|IllegalAccessException|InvocationTargetException e){
            throw new RuntimeException("Cannot create player", e);
        }
    }

}
