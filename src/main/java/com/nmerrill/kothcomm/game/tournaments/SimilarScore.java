package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.PlayerType;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.MathTools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public final class SimilarScore<T extends AbstractPlayer<T>> implements Tournament<PlayerType<T>> {
    private final Queue<PlayerType<T>> focuses;
    private final MutableList<PlayerType<T>> players;
    private final Random random;
    private final double maxDistance;

    public SimilarScore(double maxDistance, MutableList<PlayerType<T>> players, Random random) {
        focuses = new LinkedList<>();
        this.players = players;
        this.maxDistance = maxDistance;
        this.random = random;
    }

    @Override
    public MutableList<PlayerType<T>> get(int count, Scoreboard<PlayerType<T>> scoreboard) {
        MutableList<PlayerType<T>> players = rangeAround(count, poll(), scoreboard);
        if (players.size() == 1) {
            PlayerType<T> next = poll();
            if (next == players.get(0)) {
                next = poll();
            }
            players.add(next);
        }
        return players;
    }

    private PlayerType<T> poll() {
        if (focuses.isEmpty()) {
            focuses.addAll(players.shuffleThis(random));
        }
        return focuses.poll();
    }


    private MutableList<PlayerType<T>> rangeAround(int amount, PlayerType<T> player, Scoreboard<PlayerType<T>> scoreboard) {

        if (scoreboard.size() < amount) {
            return players.shuffleThis(random).subList(0, amount);
        }
        double focus = scoreboard.getScore(player);
        double minimum = focus - maxDistance;
        double maximum = focus + maxDistance;
        MutableList<PlayerType<T>> players = scoreboard.scores()
                .keyValuesView()
                .select(i -> MathTools.inRange(i.getTwo(), minimum, maximum))
                .collect(ObjectDoublePair::getOne)
                .toList();
        if (players.size() > amount) {
            return players.shuffleThis(random).subList(0, amount);
        }
        if (players.size() < amount) {
            players.addAll(
                    players.toSet()
                            .withoutAll(players).toList()
                            .shuffleThis(random)
                            .subList(0, amount - players.size())
            );
        }
        return players;
    }
}
