package game.tournaments;

import game.Game;
import game.Scoreboard;

public interface Tournament<T> extends Iterable<Game<T>> {

    default Scoreboard<T> run(){
        TournamentIterator<T> iter = iterator();
        while (iter.hasNext()){
            iter.next().run();
        }
        return iter.currentRankings();
    }

    @Override
    TournamentIterator<T> iterator();
}