package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.MathTools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.MutableObjectDoubleMap;
import org.eclipse.collections.api.ordered.OrderedIterable;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;
import org.eclipse.collections.impl.factory.primitive.ObjectDoubleMaps;


public final class MultiTournament<T> implements Tournament<T> {
    private final MutableObjectDoubleMap<Tournament<T>> tournamentsWeights;

    public MultiTournament(){
        tournamentsWeights = ObjectDoubleMaps.mutable.empty();
    }

    public void addTournament(Tournament<T> tournament, double weight){
        tournamentsWeights.put(tournament, weight);
    }

    @Override
    public MutableList<T> get(int count, Scoreboard<T> ranking) {
        OrderedIterable<ObjectDoublePair<Tournament<T>>> ordered = tournamentsWeights.keyValuesView().toList();
        MutableIntList distribution = MathTools.distribute(count, ordered.collectDouble(ObjectDoublePair::getTwo));
        return ordered.collect(ObjectDoublePair::getOne)
                .zip(distribution.collect(i->i))
                .flatCollect(p -> p.getOne().get(p.getTwo(), ranking))
                .toList();
    }
}
