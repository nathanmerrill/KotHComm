package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.game.players.PlayerType;
import com.nmerrill.kothcomm.game.players.Role;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;


public final class RoleTournament<T extends AbstractPlayer<T>> implements Tournament<PlayerType<T>> {

    private final MultiTournament<PlayerType<T>> multiTournament;
    private final Function<MutableList<PlayerType<T>>, Tournament<PlayerType<T>>> supplier;
    private final MutableList<PlayerType<T>> players;

    public RoleTournament(
            Function<MutableList<PlayerType<T>>, Tournament<PlayerType<T>>> tournamentType,
            MutableList<PlayerType<T>> players
    ){
        this.players = players.toList();
        this.multiTournament = new MultiTournament<>();
        this.supplier = tournamentType;
    }

    public RoleTournament(
            BiFunction<MutableList<PlayerType<T>>, Random, Tournament<PlayerType<T>>> tournamentType,
            MutableList<PlayerType<T>> players,
            Random random
    ){
        this(t -> tournamentType.apply(t, random), players);
    }

    public void addRole(Role<? extends T> role, double weight){
        MutableList<PlayerType<T>> filtered = players.select(role::is);
        if (!filtered.isEmpty()) {
            multiTournament.addTournament(supplier.apply(filtered), weight);
        }
    }

    @Override
    public MutableList<PlayerType<T>> get(int count, Scoreboard<PlayerType<T>> ranking) {
        return multiTournament.get(count, ranking);
    }
}
