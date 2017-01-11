package com.nmerrill.kothcomm.game.players;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;

public class Role<T> {
    private final Class<T> clazz;
    public Role(Class<T> clazz){
        this.clazz = clazz;
    }

    public final boolean is(AbstractPlayer player){
        return clazz.isInstance(player);
    }

    public final boolean is(Submission type){
        return clazz.isAssignableFrom(type.getClass());
    }

    public final T as(AbstractPlayer player){
        return clazz.cast(player);
    }

    public final MutableSet<T> filter(MutableSet<? extends AbstractPlayer> players){
        return players.select(this::is).collect(this::as);
    }

    public final MutableList<T> filter(MutableList<? extends AbstractPlayer> players){
        return players.select(this::is).collect(this::as);
    }

    public String getName(){
        return clazz.getSimpleName();
    }
}
