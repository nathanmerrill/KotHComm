package com.nmerrill.kothcomm.game.tournaments;

import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;

public interface Tournament<T>{
    MutableList<T> get(int count, Scoreboard<T> ranking);
}