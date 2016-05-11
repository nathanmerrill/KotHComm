package game.tournaments;

import game.Game;
import game.Scoreboard;

import java.util.Iterator;

public interface TournamentIterator<T> extends Iterator<Game<T>> {
    Scoreboard<T> currentRankings();
}
