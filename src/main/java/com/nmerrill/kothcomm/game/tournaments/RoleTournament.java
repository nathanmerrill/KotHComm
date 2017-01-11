package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.game.players.Submission;
import com.nmerrill.kothcomm.game.players.Role;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;


public final class RoleTournament<T extends AbstractPlayer<T>> implements Tournament<Submission<T>> {

    private final MultiTournament<Submission<T>> multiTournament;
    private final Function<MutableList<Submission<T>>, Tournament<Submission<T>>> supplier;
    private final MutableList<Submission<T>> players;

    public RoleTournament(
            Function<MutableList<Submission<T>>, Tournament<Submission<T>>> tournamentType,
            MutableList<Submission<T>> players
    ){
        this.players = players.toList();
        this.multiTournament = new MultiTournament<>();
        this.supplier = tournamentType;
    }

    public RoleTournament(
            BiFunction<MutableList<Submission<T>>, Random, Tournament<Submission<T>>> tournamentType,
            MutableList<Submission<T>> players,
            Random random
    ){
        this(t -> tournamentType.apply(t, random), players);
    }

    public void addRole(Role<? extends T> role, double weight){
        MutableList<Submission<T>> filtered = players.select(role::is);
        if (!filtered.isEmpty()) {
            multiTournament.addTournament(supplier.apply(filtered), weight);
        }
    }

    @Override
    public MutableList<Submission<T>> get(int count, Scoreboard<Submission<T>> ranking) {
        return multiTournament.get(count, ranking);
    }
}
