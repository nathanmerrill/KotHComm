package game;

import game.exceptions.PlayerNotFoundException;
import utils.iterables.Pair;
import utils.iterables.WithIndex;

import java.util.*;

public class PlayerRanking<T> implements Iterable<PlayerType<T>> {
    private final List<PlayerType<T>> rankings;

    public PlayerRanking(){
        rankings = new ArrayList<>();
    }

    public PlayerRanking(Collection<PlayerType<T>> rankings){
        this();
        this.rankings.addAll(rankings);
    }

    public void rankTop(PlayerType<T> player){
        insertAt(player, 0);
    }

    public void rankBottom(PlayerType<T> player){
        insertAt(player, rankings.size());
    }

    public void rankBelow(PlayerType<T> player, PlayerType<T> other){
        insertAt(player, getIndex(other)+1);
    }

    public void rankAbove(PlayerType<T> player, PlayerType<T> other){
        insertAt(player, getIndex(other));
    }

    public int getRank(PlayerType<T> player){
        return getIndex(player)+1;
    }

    public void setRank(PlayerType<T> player, int rank){
        rank--;
        if (rank < 0) {
            rank = 0;
        } else if (rank > rankings.size()){
            rank = rankings.size();
        }
        insertAt(player, rank);
    }

    private int getIndex(PlayerType<T> player){
        int index = rankings.indexOf(player);
        if (index == -1){
            throw new PlayerNotFoundException();
        }
        return index;
    }

    private void insertAt(PlayerType<T> player, int position){
        rankings.remove(player);
        rankings.add(position, player);
    }

    @Override
    public ListIterator<PlayerType<T>> iterator() {
        return rankings.listIterator();
    }

    public List<PlayerType<T>> getPlayers(){
        return new ArrayList<>(rankings);
    }

    public List<Pair<Integer, PlayerType<T>>> getRankings(){
        List<PlayerType<T>> players = new ArrayList<>(rankings);
        players.add(0, null); //Insert null value at position 0 to offset indices
        List<Pair<Integer, PlayerType<T>>> indexes = new ArrayList<>();
        new WithIndex<>(players).forEach(indexes::add);
        indexes.remove(0);
        return indexes;
    }

}
