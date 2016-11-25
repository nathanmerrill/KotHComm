package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public final class Sampling<T extends AbstractPlayer<T>> implements Tournament<PlayerType<T>> {
    final Queue<PlayerType<T>> currentPopulation;
    private final Random random;
    final MutableList<PlayerType<T>> availablePlayers;

    public Sampling(MutableList<PlayerType<T>> players, Random random) {
        availablePlayers = players;
        this.random = random;
        currentPopulation = new LinkedList<>(availablePlayers);
        repopulate(null);
    }

    private void repopulate(MutableSet<PlayerType<T>> ignore) {
        MutableList<PlayerType<T>> next = availablePlayers.clone();
        if (ignore != null) {
            next.removeIf(ignore::contains);
        }
        currentPopulation.addAll(next.shuffleThis(random));
    }

    @Override
    public MutableList<PlayerType<T>> get(int amount, Scoreboard<PlayerType<T>> scoreboard) {
        if (currentPopulation.size() < amount) {
            repopulate(Sets.mutable.ofAll(currentPopulation));
        }
        return Lists.mutable.withNValues(amount, currentPopulation::poll);
    }

}




