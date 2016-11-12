package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.MathTools;
import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.GameManager;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;

import java.util.*;


public final class SimilarScore<T extends AbstractPlayer<T>> implements Tournament<T> {
    private final Queue<PlayerType<T>> focuses;
    private final GameManager<T> manager;
    private final double maxDistance;

    public SimilarScore(GameManager<T> manager, double maxDistance) {
        focuses = new LinkedList<>();
        this.manager = manager;
        this.maxDistance = maxDistance;
    }

    @Override
    public AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard) {
        MutableList<PlayerType<T>> players = rangeAround(poll(), scoreboard);
        if (players.size() == 1) {
            PlayerType<T> next = poll();
            if (next == players.get(0)) {
                next = poll();
            }
            players.add(next);
        }
        return manager.constructFromType(players);
    }

    private PlayerType<T> poll() {
        if (focuses.isEmpty()) {
            focuses.addAll(manager.allPlayers().shuffleThis(manager.getRandom()));
        }
        return focuses.poll();
    }


    private MutableList<PlayerType<T>> rangeAround(PlayerType<T> player, Scoreboard<PlayerType<T>> scoreboard) {

        if (scoreboard.size() < manager.minPlayerCount()) {
            return manager.allPlayers().shuffleThis().subList(0, manager.gameSize());
        }
        double focus = scoreboard.getScore(player);
        double minimum = focus - maxDistance;
        double maximum = focus + maxDistance;
        MutableList<PlayerType<T>> players = scoreboard.scores()
                .keyValuesView()
                .select(i -> MathTools.inRange(i.getTwo(), minimum, maximum))
                .collect(ObjectDoublePair::getOne)
                .toList();
        if (players.size() > manager.maxPlayerCount()) {
            return players.shuffleThis(manager.getRandom()).subList(0, manager.maxPlayerCount());
        }
        if (players.size() < manager.minPlayerCount()) {
            players.addAll(
                    manager.allPlayers().toSet()
                            .withoutAll(players).toList()
                            .shuffleThis(manager.getRandom())
                            .subList(0, manager.minPlayerCount() - players.size())
            );
        }
        return players;
    }
}
