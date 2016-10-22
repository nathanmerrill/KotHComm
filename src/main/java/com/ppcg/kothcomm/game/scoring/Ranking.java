package com.ppcg.kothcomm.game.scoring;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.api.tuple.Pair;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

public interface Ranking<T> extends Comparator<T>, Iterable<T> {

    MutableSortedSet<T> rank();

    default int size(){
        return rank().size();
    }

    default boolean isEmpty() {
        return rank().isEmpty();
    }

    default boolean contains(T item){
        return rank().contains(item);
    }

    @Override
    default Iterator<T> iterator() {
        return rank().iterator();
    }

    default MutableList<T> toList() {
        return rank().toList();
    }

    @Override
    default int compare(T o1, T o2) {
        if (Objects.equals(o1, o2)){
            return 0;
        }
        MutableSortedSet<T> ranking = rank();
        Comparator<? super T> comparator = ranking.comparator();
        if (comparator != null){
            return comparator.compare(o1, o2);
        }
        if (!ranking.contains(o1)){
            return 1;
        }
        if (!ranking.contains(o2)){
            return -1;
        }
        if (ranking.headSet(o1).contains(o2)){
            return 1;
        }
        return -1;
    }

    public default String scoreTable() {
        StringBuilder builder = new StringBuilder();
        MutableSortedSet<T> ranking = rank();
        if (ranking.isEmpty()){
            return "No scores";
        }
        Comparator<? super T> comparator = ranking.comparator();
        int currentRank = 1;
        T lastItem = null;
        for (Pair<T, Integer> item :ranking.zipWithIndex()){
            if (lastItem == null || comparator == null
                    || comparator.compare(item.getOne(), lastItem) != 0){
                currentRank = item.getTwo()+1;
            }
            lastItem = item.getOne();
            builder.append(currentRank).append(".\t");
            builder.append(lastItem.toString().split("\n")[0]);
            builder.append('\n');
        }
        return builder.toString();
    }

    static <T> Ranking<T> toRanking(MutableList<T> ordered){
        return ordered::toSortedSet;
    }
}
