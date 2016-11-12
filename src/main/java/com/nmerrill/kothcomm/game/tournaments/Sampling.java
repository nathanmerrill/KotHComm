package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.GameManager;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.util.*;


public final class Sampling<T extends AbstractPlayer<T>> implements Tournament<T> {
    private final GameManager<T> manager;
    final Queue<PlayerType<T>> currentPopulation;
    final MutableList<PlayerType<T>> availablePlayers;
    final int gameSize;

    public Sampling(GameManager<T> manager) {
        this.manager = manager;
        availablePlayers = manager.allPlayers();
        currentPopulation = new LinkedList<>(availablePlayers);
        gameSize = manager.gameSize();
        repopulate(null);
    }

    private void repopulate(MutableSet<PlayerType<T>> ignore) {
        MutableList<PlayerType<T>> next = availablePlayers.clone();
        if (ignore != null) {
            next.removeIf(ignore::contains);
        }
        currentPopulation.addAll(next.shuffleThis(manager.getRandom()));
    }

    @Override
    public AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard) {
        if (currentPopulation.size() < gameSize) {
            repopulate(Sets.mutable.ofAll(currentPopulation));
        }
        return manager.constructFromType(Lists.mutable.withNValues(gameSize, currentPopulation::poll));
    }

}




