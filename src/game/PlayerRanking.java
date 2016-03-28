package game;

import game.exceptions.PlayerNotFoundException;
import utils.iterables.Pair;
import utils.iterables.WithIndex;

import java.util.*;

public class PlayerRanking implements Iterable<Player> {
    private final List<Player> rankings;

    public PlayerRanking(){
        rankings = new ArrayList<>();
    }

    public PlayerRanking(Collection<Player> rankings){
        this();
        this.rankings.addAll(rankings);
    }

    public void rankTop(Player player){
        insertAt(player, 0);
    }

    public void rankBottom(Player player){
        insertAt(player, rankings.size());
    }

    public void rankBelow(Player player, Player other){
        insertAt(player, getIndex(other)+1);
    }

    public void rankAbove(Player player, Player other){
        insertAt(player, getIndex(other));
    }

    public int getRank(Player player){
        return getIndex(player)+1;
    }

    public void setRank(Player player, int rank){
        rank--;
        if (rank < 0) {
            rank = 0;
        } else if (rank > rankings.size()){
            rank = rankings.size();
        }
        insertAt(player, rank);
    }

    private int getIndex(Player player){
        int index = rankings.indexOf(player);
        if (index == -1){
            throw new PlayerNotFoundException();
        }
        return index;
    }

    private void insertAt(Player player, int position){
        rankings.remove(player);
        rankings.add(position, player);
    }

    @Override
    public ListIterator<Player> iterator() {
        return rankings.listIterator();
    }

    public List<Player> getPlayers(){
        return new ArrayList<>(rankings);
    }

    public List<Pair<Integer, Player>> getRankings(){
        List<Player> players = new ArrayList<>(rankings);
        players.add(0, null); //Insert null value at position 0 to offset indices
        List<Pair<Integer, Player>> indexes = new ArrayList<>();
        new WithIndex<>(players).forEach(indexes::add);
        indexes.remove(0);
        return indexes;
    }

}
