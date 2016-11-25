package com.nmerrill.kothcomm.game.scoring;

import org.eclipse.collections.api.DoubleIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableDoubleList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;
import org.eclipse.collections.impl.factory.primitive.DoubleLists;

import java.util.function.Function;

public final class ItemAggregator<T> implements Aggregator<Scoreboard<T>> {

    private final Function<MutableDoubleList, Double> aggregator;


    public ItemAggregator(Function<MutableDoubleList, Double> aggregator) {
        this.aggregator = aggregator;
    }

    public ItemAggregator(){
        this(DoubleIterable::sum);
    }

    @Override
    public Scoreboard<T> aggregate(MutableList<? extends Scoreboard<T>> scores) {
        MutableMap<T, MutableDoubleList> map = scores.flatCollect(s -> s.scores().keyValuesView())
                .aggregateInPlaceBy(ObjectDoublePair::getOne, DoubleLists.mutable::empty, (list, d) -> list.add(d.getTwo()));
        Scoreboard<T> scoreboard = new Scoreboard<>();
        map.forEach((i,l) -> scoreboard.setScore(i, aggregator.apply(l)));
        return scoreboard;
    }
}
