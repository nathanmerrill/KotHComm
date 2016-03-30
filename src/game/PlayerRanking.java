package game;

import game.exceptions.PlayerNotFoundException;
import utils.iterables.Pair;
import utils.iterables.WithIndex;

import java.util.*;

public class PlayerRanking implements Iterable<PlayerType> {
    private final List<PlayerType> rankings;

    public PlayerRanking(){
        rankings = new ArrayList<>();
    }

    public PlayerRanking(Collection<PlayerType> rankings){
        this();
        this.rankings.addAll(rankings);
    }

    public void rankTop(PlayerType player){
        insertAt(player, 0);
    }

    public void rankBottom(PlayerType player){
        insertAt(player, rankings.size());
    }

    public void rankBelow(PlayerType player, PlayerType other){
        insertAt(player, getIndex(other)+1);
    }

    public void rankAbove(PlayerType player, PlayerType other){
        insertAt(player, getIndex(other));
    }

    public int getRank(PlayerType player){
        return getIndex(player)+1;
    }

    public void setRank(PlayerType player, int rank){
        rank--;
        if (rank < 0) {
            rank = 0;
        } else if (rank > rankings.size()){
            rank = rankings.size();
        }
        insertAt(player, rank);
    }

    private int getIndex(PlayerType player){
        int index = rankings.indexOf(player);
        if (index == -1){
            throw new PlayerNotFoundException();
        }
        return index;
    }

    private void insertAt(PlayerType player, int position){
        rankings.remove(player);
        rankings.add(position, player);
    }

    @Override
    public ListIterator<PlayerType> iterator() {
        return rankings.listIterator();
    }

    public List<PlayerType> getPlayers(){
        return new ArrayList<>(rankings);
    }

    public List<Pair<Integer, PlayerType>> getRankings(){
        List<PlayerType> players = new ArrayList<>(rankings);
        players.add(0, null); //Insert null value at position 0 to offset indices
        List<Pair<Integer, PlayerType>> indexes = new ArrayList<>();
        new WithIndex<>(players).forEach(indexes::add);
        indexes.remove(0);
        return indexes;
    }

}
