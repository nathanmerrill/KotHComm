package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.AbstractGame;
import com.ppcg.kothcomm.game.AbstractPlayer;
import com.ppcg.kothcomm.game.GameManager;
import com.ppcg.kothcomm.game.PlayerType;
import com.ppcg.kothcomm.game.scoreboards.Scoreboard;
import com.ppcg.kothcomm.utils.Tools;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
            List<PlayerType<T>> players = rangeAround(poll(), scoreboard);
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
                List<PlayerType<T>> next = new ArrayList<>(manager.allPlayers());
                Collections.shuffle(next);
                focuses.addAll(next);
            }
            return focuses.poll();
        }


        private List<PlayerType<T>> rangeAround(PlayerType<T> player, Scoreboard<PlayerType<T>> scoreboard) {
            int gameSize = manager.gameSize();
            if (scoreboard.size() < gameSize){
                if (scoreboard.isEmpty()){
                    List<PlayerType<T>> players =  manager.allPlayers();
                    Collections.shuffle(players);
                    return players.subList(0, players.size());
                }
                return scoreboard.items();
            }
            double focus = scoreboard.getScore(player);
            double minimum = focus - maxDistance;
            double maximum = focus + maxDistance;
            return scoreboard.stream()
                    .filter(i -> Tools.inRange(scoreboard.getScore(i), minimum, maximum))
                    .collect(Collectors.toList());
        }
    }
}
