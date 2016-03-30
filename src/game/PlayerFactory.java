package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class PlayerFactory {
    private final Map<String, Supplier<Player>> factories;
    public PlayerFactory(){
        factories = new HashMap<>();
    }
    public void registerPlayer(String player, Supplier<Player> constructor){
        factories.put(player, constructor);
    }
    public List<String> registeredPlayers(){
        return new ArrayList<>(factories.keySet());
    }
    public Player createPlayer(String name){
        return factories.get(name).get();
    }
    public List<Player> createPlayers(List<String> names){
        return names.stream().map(this::createPlayer).collect(Collectors.toList());
    }
}
