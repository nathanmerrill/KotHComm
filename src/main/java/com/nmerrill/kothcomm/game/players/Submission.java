package com.nmerrill.kothcomm.game.players;

import java.util.function.Supplier;

public final class Submission<T extends AbstractPlayer<T>> implements Comparable<Submission<T>>{
    private final Supplier<T> supplier;
    private final String name;

    public Submission(String name, Supplier<T> supplier){
        this.supplier = supplier;
        this.name = name;
    }

    public T create(){
        T player = supplier.get();
        player.setType(this);
        return player;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Submission that = (Submission) o;

        return name.equals(that.name);
    }

    @Override
    public int compareTo(Submission<T> o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
