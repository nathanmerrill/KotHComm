package com.nmerrill.kothcomm.game.scoring;

import org.eclipse.collections.api.bag.Bag;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.primitive.ObjectIntPair;
import org.eclipse.collections.impl.factory.Bags;
import org.eclipse.collections.impl.factory.Lists;

import java.util.List;

public final class STVAggregator<T> implements Aggregator<Scoreboard<T>> {

    @Override
    public Scoreboard<T> aggregate(MutableList<? extends Scoreboard<T>> scores) {
        MutableBag<MutableList<MutableSet<T>>> votes = scores.collect(Scoreboard::rank).toBag();
        Scoreboard<T> scoreboard = new Scoreboard<>();
        scores.flatCollect(Scoreboard::items).forEachWith(scoreboard::addScore, 0.0);

        MutableBag<List<Integer>> set = Bags.mutable.empty();
        set.add(Lists.mutable.empty());

        while (!votes.isEmpty()) {
            MutableSet<T> losers = countVotes(votes).bottomOccurrences(1)
                    .tap(p -> scoreboard.addScore(p.getOne(), p.getTwo()))
                    .collect(ObjectIntPair::getOne).toSet();
            MutableBag<MutableList<MutableSet<T>>> transferredVotes =
                    votes.select(list -> list.getFirst().anySatisfy(losers::contains));
            votes.removeAll(transferredVotes);
            transferredVotes.forEachWithOccurrences((list, count)-> {
                if (list.size() > 2){
                    votes.addOccurrences(list.subList(1, list.size()), count);
                }
            });
        }
        scoreboard.setDescending();
        return scoreboard;
    }


    public MutableBag<T> countVotes(Bag<MutableList<MutableSet<T>>> votes){
        MutableBag<T> counts = Bags.mutable.empty();
        votes.forEachWithOccurrences((list, count)-> list.getFirst().forEachWith(counts::addOccurrences, count));
        return counts;
    }


}
