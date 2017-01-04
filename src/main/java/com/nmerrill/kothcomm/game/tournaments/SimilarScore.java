package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.MathTools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public final class SimilarScore<T> implements Tournament<T> {
    private final Queue<T> focuses;
    private final MutableList<T> players;
    private final Random random;
    private double maxDistance;

    public SimilarScore(double maxDistance, MutableList<T> players, Random random) {
        focuses = new LinkedList<>();
        this.players = players;
        this.maxDistance = maxDistance;
        this.random = random;
    }

    public SimilarScore(MutableList<T> players, Random random) {
        this(-1, players, random);
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    @Override
    public MutableList<T> get(int count, Scoreboard<T> scoreboard) {
        if (maxDistance < 0){
            throw new RuntimeException("Maximum distance must be set");
        }
        MutableList<T> players = rangeAround(count, poll(), scoreboard);
        if (players.size() == 1) {
            T next = poll();
            if (next == players.get(0)) {
                next = poll();
            }
            players.add(next);
        }
        return players;
    }

    private T poll() {
        if (focuses.isEmpty()) {
            focuses.addAll(players.shuffleThis(random));
        }
        return focuses.poll();
    }


    private MutableList<T> rangeAround(int amount, T player, Scoreboard<T> scoreboard) {

        if (scoreboard.size() < amount) {
            return players.shuffleThis(random).subList(0, amount);
        }
        double focus = scoreboard.getScore(player);
        double minimum = focus - maxDistance;
        double maximum = focus + maxDistance;
        MutableList<T> players = scoreboard.scores()
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
