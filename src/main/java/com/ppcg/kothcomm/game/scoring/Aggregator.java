package com.ppcg.kothcomm.game.scoring;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;

public interface Aggregator<T> {

    T aggregate(MutableList<T> scores);

}
