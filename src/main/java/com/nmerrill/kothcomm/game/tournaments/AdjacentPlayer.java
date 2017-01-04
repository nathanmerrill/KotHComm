package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public final class AdjacentPlayer<T> implements Tournament<T> {
    private final Queue<T> focuses;
    private final MutableList<T> players;
    private final Random random;

    public AdjacentPlayer(MutableList<T> players, Random random) {
        this.players = players;
        this.random = random;
        focuses = new LinkedList<>();
    }

    @Override
    public MutableList<T> get(int count, Scoreboard<T> scoreboard) {
        if (focuses.isEmpty()) {
            focuses.addAll(scoreboard.items().toList().shuffleThis(random));
        }
        return rangeAround(count, focuses.poll(), scoreboard);
    }

    private <U> MutableList<U> rangeAround(int gameSize, U player, Scoreboard<U> scoreboard) {
        MutableList<U> sorted = scoreboard.scoresOrdered().collect(ObjectDoublePair::getOne);
        if (sorted.size() < gameSize) {
            return sorted;
        }
        int minIndex = sorted.indexOf(player);
        int maxIndex = minIndex;
        while (maxIndex - minIndex + 1 < gameSize) {
            if (minIndex == 0) {
                maxIndex = gameSize - 1;
                break;
            }
            if (maxIndex == sorted.size() - 1) {
                minIndex = sorted.size() - gameSize;
                break;
            }
            double minDiff = scoreboard.getScore(sorted.get(minIndex)) - scoreboard.getScore(sorted.get(minIndex - 1));
            double maxDiff = scoreboard.getScore(sorted.get(maxIndex + 1)) - scoreboard.getScore(sorted.get(maxIndex));
            if (maxDiff < minDiff) {
                maxIndex++;
            } else {
                minIndex--;
            }
        }
        return sorted.subList(minIndex, maxIndex + 1);
    }
}

