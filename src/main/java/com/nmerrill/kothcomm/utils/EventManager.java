package com.nmerrill.kothcomm.utils;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.tuple.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EventManager<T> {
    private final MutableMultimap<T, Function<Event, Boolean>> listeners;
    private final MutableMap<Integer, Function<Event, Boolean>> eventNums;
    private final MutableMap<Function<Event, Boolean>, Pair<Integer, T>> reverseMapping;
    private int counter;

    public EventManager(){
        listeners = Multimaps.mutable.list.empty();
        eventNums = Maps.mutable.empty();
        reverseMapping = Maps.mutable.empty();
        counter = 1;
    }

    public int addListener(T type, Function<Event, Boolean> listener){
        listeners.put(type, listener);
        eventNums.put(counter, listener);
        reverseMapping.put(listener, Tuples.pair(0, type));
        return counter++;
    }

    public void removeListener(int key){
        removeListener(eventNums.get(key));
    }

    public void removeListener(Function<Event, Boolean> listener){
        Pair<Integer, T> keys = reverseMapping.get(listener);
        if (keys != null){
            eventNums.remove(keys.getOne());
            listeners.get(keys.getTwo()).remove(listener);
        }
    }

    public void addEvent(Event event, T type){
        List<Function<Event, Boolean>> toRemove = new ArrayList<>();
        listeners.get(type).forEach(f -> {
            if (!f.apply(event)){
                toRemove.add(f);
            }
        });
        toRemove.forEach(this::removeListener);
    }

}
