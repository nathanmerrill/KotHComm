package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.AbstractGame;
import com.ppcg.kothcomm.game.AbstractPlayer;
import com.ppcg.kothcomm.game.GameManager;
import com.ppcg.kothcomm.game.PlayerType;
import com.ppcg.kothcomm.game.scoring.Scoreboard;
import com.ppcg.kothcomm.utils.Tools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;

import java.util.*;
import java.util.function.Supplier;

public class SimilarScoreProvider<T extends AbstractPlayer<T>> implements Supplier<GameProvider<T>> {

    private final GameManager<T> manager;
    private final double maxDistance;

    public SimilarScoreProvider(GameManager<T> manager, double maxDistance){
        this.manager = manager;
        this.maxDistance = maxDistance;
    }

    @Override
    public SimilarScore get() {
        return new SimilarScore();
    }

    public class SimilarScore implements GameProvider<T> {
        private final Queue<PlayerType<T>> focuses;

        public SimilarScore() {
            focuses = new LinkedList<>();
        }

        @Override
        public AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard) {
            MutableList<PlayerType<T>> players = rangeAround(poll(), scoreboard);
            if (players.size() == 1){
                PlayerType<T> next = poll();
                if (next == players.get(0)){
                    next = poll();
                }
                players.add(next);
            }
            return manager.constructFromType(players);
        }

        private PlayerType<T> poll(){
            if (focuses.isEmpty()) {
                focuses.addAll(manager.allPlayers().shuffleThis(manager.getRandom()));
            }
            return focuses.poll();
        }


        private MutableList<PlayerType<T>> rangeAround(PlayerType<T> player, Scoreboard<PlayerType<T>> scoreboard) {

            if (scoreboard.size() < manager.minPlayerCount()){
                return manager.allPlayers().shuffleThis().subList(0, manager.gameSize());
            }
            double focus = scoreboard.getScore(player);
            double minimum = focus - maxDistance;
            double maximum = focus + maxDistance;
            MutableList<PlayerType<T>> players = scoreboard.scores()
                    .keyValuesView()
                    .select(i -> Tools.inRange(i.getTwo(), minimum, maximum))
                    .collect(ObjectDoublePair::getOne)
                    .toList();
            if (players.size() > manager.maxPlayerCount()){
                return players.shuffleThis(manager.getRandom()).subList(0, manager.maxPlayerCount());
            }
            if (players.size() < manager.minPlayerCount()){
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
}
