package com.nmerrill.kothcomm.game.scoring;

import org.eclipse.collections.api.list.MutableList;

public interface Aggregator<T> {

    T aggregate(MutableList<? extends T> scores);

}
