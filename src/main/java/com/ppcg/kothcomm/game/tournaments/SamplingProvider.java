package com.ppcg.kothcomm.game.tournaments;

import com.ppcg.kothcomm.game.*;
import com.ppcg.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.util.*;
import java.util.function.Supplier;


public class SamplingProvider<T extends AbstractPlayer<T>> implements Supplier<GameProvider<T>> {

    private final GameManager<T> manager;

    public SamplingProvider(GameManager<T> manager) {
        this.manager = manager;
    }

    @Override
    public Sampling get() {
        return new Sampling();
    }

    public class Sampling implements GameProvider<T> {
        final Queue<PlayerType<T>> currentPopulation;
        final MutableList<PlayerType<T>> availablePlayers;
        final int gameSize;

        public Sampling(){
            availablePlayers = manager.allPlayers();
            currentPopulation = new LinkedList<>(availablePlayers);
            gameSize = manager.gameSize();
            repopulate(null);
        }

        private void repopulate(MutableSet<PlayerType<T>> ignore){
            MutableList<PlayerType<T>> next = availablePlayers.clone();
            if (ignore != null) {
                next.removeIf(ignore::contains);
            }
            currentPopulation.addAll(next.shuffleThis(manager.getRandom()));
        }

        @Override
        public AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard) {
            if (currentPopulation.size() < gameSize){
                repopulate(Sets.mutable.ofAll(currentPopulation));
            }
            return manager.constructFromType(Lists.mutable.withNValues(gameSize, currentPopulation::poll));
        }

    }



}
