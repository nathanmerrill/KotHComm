package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.GameManager;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;

import java.util.*;


public final class AdjacentPlayer<T extends AbstractPlayer<T>> implements Tournament<T> {
    private final Queue<PlayerType<T>> focuses;
    private final GameManager<T> manager;

    public AdjacentPlayer(GameManager<T> manager) {
        this.manager = manager;
        focuses = new LinkedList<>();
    }

    @Override
    public AbstractGame<T> get(Scoreboard<PlayerType<T>> scoreboard) {
        if (focuses.isEmpty()) {
            focuses.addAll(scoreboard.items().toList().shuffleThis(manager.getRandom()));
        }
        MutableList<PlayerType<T>> players = rangeAround(focuses.poll(), scoreboard);
        return manager.constructFromType(players);
    }

    private <U> MutableList<U> rangeAround(U player, Scoreboard<U> scoreboard) {
        MutableList<U> sorted = scoreboard.scoresOrdered().collect(ObjectDoublePair::getOne);
        int gameSize = manager.gameSize();
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

