package com.ppcg.kothcomm.game;

import com.ppcg.kothcomm.game.scoreboards.AggregateScoreboard;
import com.ppcg.kothcomm.utils.Pair;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ScoreboardTester {
    private Object player;
    private Object alternate;
    @BeforeClass
    public void createPlayer(){
        player = new Object();
        alternate = new Object();
    }

    @Test
    public void createScoreboard(){
        AggregateScoreboard<Object> scoreboard = new AggregateScoreboard<>();
        Assert.assertEquals(scoreboard.getScores().size(), 0, "Scoreboard isn't empty");
        Assert.assertEquals(scoreboard.getScore(player), 0.0, "Player shouldn't have a score");
        Assert.assertEquals(scoreboard.getScores().size(), 0, "Scoreboard changes after getting the score of a player");
        scoreboard.stream().forEach(i -> Assert.assertTrue(false, "Scoreboard stream contains a player"));
        scoreboard.clear();
        Assert.assertEquals(scoreboard.getPlayerHistory(player).size(), 0, "Player shouldn't have a history");
        Assert.assertEquals(scoreboard.items().size(), 0, "There should be no players");
        scoreboard.toString();
    }

    @Test
    public void addPlayer(){
        AggregateScoreboard<Object> scoreboard = new AggregateScoreboard<>();
        scoreboard.addScore(player, 20);
        Assert.assertEquals(scoreboard.getScore(player), 20.0, "Score not updated");
        Assert.assertEquals(scoreboard.items().size(), 1, "Scoreboard not storing players");
        Assert.assertTrue(scoreboard.items().contains(player), "Players don't match");
        scoreboard.toString();
        scoreboard.addScore(alternate, 50);
        Assert.assertEquals(scoreboard.itemsOrdered().size(), 2, "Scoreboard not storing multiple players");
        Assert.assertEquals(scoreboard.getScore(alternate), 50.0, "Scoreboard not updating second player correctly");
        Assert.assertEquals(scoreboard.getScore(player), 20.0, "Scoreboard incorrectly updating first player when adding second");
        Object topPlayer = scoreboard.itemsOrdered().get(0);
        Assert.assertNotNull(topPlayer, "Scoreboard returning null players");
        Assert.assertEquals(topPlayer, alternate, "Scoring order not being respected");
        AggregateScoreboard<Object> minScoreboard = new AggregateScoreboard<>();
        minScoreboard.setMaxOrdered(false);
        minScoreboard.addScore(player, 20);
        minScoreboard.addScore(alternate, 50);
        Assert.assertEquals(minScoreboard.items().get(0), player, "Min scoring not being respected");
    }

    @Test
    public void scoringMethods(){
        Map<Object, Double> scores = new HashMap<>();
        scores.put(player, 15.0);
        scores.put(player, 50.0);
        scores.put(player, 25.0);

        AggregateScoreboard<Object> maxScoreboard = new AggregateScoreboard<>(AggregateScoreboard::maxAggregator);
        AggregateScoreboard<Object> minScoreboard = new AggregateScoreboard<>(AggregateScoreboard::minAggregator);
        AggregateScoreboard<Object> averageScoreboard = new AggregateScoreboard<>(AggregateScoreboard::meanAggregator);
        AggregateScoreboard<Object> summingScoreboard = new AggregateScoreboard<>(AggregateScoreboard::sumAggregator);

        maxScoreboard.addScores(scores);
        minScoreboard.addScores(scores);
        averageScoreboard.addScores(scores);
        summingScoreboard.addScores(scores);

        Assert.assertEquals(maxScoreboard.getScore(player), 50.0, "Maximum not properly scoring");
        Assert.assertEquals(minScoreboard.getScore(player), 15.0, "Minimum not properly scoring");
        Assert.assertEquals(averageScoreboard.getScore(player), 30.0, 0.0001, "Average not properly scoring");
        Assert.assertEquals(summingScoreboard.getScore(player), 90.0, 0.0001, "Summing not properly scoring");
    }
}
