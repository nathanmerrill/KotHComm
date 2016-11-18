package com.nmerrill.kothcomm.game.scoring;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ScoreboardTester {
    @Test
    public void testDefaultOrdering(){
        Object first = new Object();
        Object second = new Object();
        Scoreboard<Object> scoreboard = new Scoreboard<>();
        scoreboard.addScore(first, 0);
        scoreboard.addScore(second, 1);
        Assert.assertTrue(scoreboard.compare(first, second) < 0);
        Assert.assertEquals(2, scoreboard.scoresOrdered().size());
        Assert.assertEquals(first, scoreboard.scoresOrdered().get(0).getOne());
        Assert.assertEquals(second, scoreboard.scoresOrdered().get(1).getOne());
    }


    @Test
    public void testAscendingOrdering(){
        Object first = new Object();
        Object second = new Object();
        Scoreboard<Object> scoreboard = new Scoreboard<>();
        scoreboard.addScore(first, 0);
        scoreboard.addScore(second, 1);
        scoreboard.setAscending();
        Assert.assertTrue(scoreboard.compare(first, second) < 0);
        Assert.assertEquals(2, scoreboard.scoresOrdered().size());
        Assert.assertEquals(first, scoreboard.scoresOrdered().get(0).getOne());
        Assert.assertEquals(second, scoreboard.scoresOrdered().get(1).getOne());
    }


    @Test
    public void testDescendingOrdering(){
        Object first = new Object();
        Object second = new Object();
        Scoreboard<Object> scoreboard = new Scoreboard<>();
        scoreboard.addScore(second, 1);
        scoreboard.addScore(first, 0);
        scoreboard.setDescending();
        Assert.assertTrue(scoreboard.compare(first, second) > 0);
        Assert.assertEquals(2, scoreboard.scoresOrdered().size());
        Assert.assertEquals(second, scoreboard.scoresOrdered().get(0).getOne());
        Assert.assertEquals(first, scoreboard.scoresOrdered().get(1).getOne());
    }
}
