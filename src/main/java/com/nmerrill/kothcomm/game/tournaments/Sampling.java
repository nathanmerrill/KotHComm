package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public final class Sampling<T> implements Tournament<T> {
    final Queue<T> currentPopulation;
    private final Random random;
    final MutableList<T> availablePlayers;

    public Sampling(MutableList<T> players, Random random) {
        availablePlayers = players;
        this.random = random;
        currentPopulation = new LinkedList<>(availablePlayers);
        repopulate(null);
    }



    private void repopulate(MutableSet<T> ignore) {
        MutableList<T> next = availablePlayers.clone();
        if (ignore != null) {
            next.removeIf(ignore::contains);
        }
        currentPopulation.addAll(next.shuffleThis(random));
    }

    @Override
    public MutableList<T> get(int amount, Scoreboard<T> scoreboard) {
        if (currentPopulation.size() < amount) {
            repopulate(Sets.mutable.ofAll(currentPopulation));
        }
        return Lists.mutable.withNValues(amount, currentPopulation::poll);
    }

}




