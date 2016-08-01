package game;

import java.io.File;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Directory<T extends GamePlayer> {
    public void registerDirectory(Function<String, T> constructor, String directory){
        File file = new File(System.getProperty("user.dir"),directory);
        File[] children = file.listFiles();
        if (children == null){
            throw new RuntimeException("Cannot find directory:"+file.getAbsolutePath());
        }
        for (File child: children){
            if (child.isDirectory()){
                register(child.getName(), () -> constructor.apply(child.getAbsolutePath()));
            }
        }
    }
    private final Map<String, PlayerType<T>> registry;
    public Directory(){
        registry = new HashMap<>();
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

    public void register(Class<? extends GamePlayer> clazz, Supplier<T> supplier){
        register(clazz.getSimpleName(), supplier);
    }

    public void register(String name, Supplier<T> supplier){
        registry.put(name, new PlayerType<>(name, supplier));
    }

    public int playerCount(){
        return registry.size();
    }

}
