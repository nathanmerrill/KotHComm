package com.nmerrill.kothcomm.sample_games.rps;

import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.utils.iterables.PermutationIterator;
import com.nmerrill.kothcomm.utils.iterables.SubsequenceIterable;
import org.eclipse.collections.api.list.MutableList;

import java.util.Iterator;

public class RPSGame extends AbstractGame<RPSPlayer> {

    private Scoreboard<RPSPlayer> scores;

    private Iterator<MutableList<RPSPlayer>> pairings;


    @Override
    protected void setup() {
        pairings = new SubsequenceIterable<>(players, 2, PermutationIterator::new).iterator();
        scores = new Scoreboard<>();
    }

    @Override
    public boolean finished() {
        return !pairings.hasNext();
    }

    @Override
    public Scoreboard<RPSPlayer> getScores() {
        return scores;
    }

    @Override
    protected void step() {
        if (finished()){
            return;
        }
        MutableList<RPSPlayer> pair = pairings.next();
        int winner = getWinner(pair.collect(RPSPlayer::makeChoice));
        if (winner == -1){
            scores.addScores(pair, .5);
        } else {
            scores.addScore(pair.get(winner), 1);
        }
    }

    private int getWinner(MutableList<Choice> choices){
        return (choices.get(0).ordinal() - choices.get(1).ordinal() + 3)%3 -1;
    }
}
