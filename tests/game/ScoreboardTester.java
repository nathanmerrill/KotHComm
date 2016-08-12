package game;

import KoTHComm.game.Scoreboard;
import KoTHComm.utils.Pair;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

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
        Scoreboard<Object> scoreboard = new Scoreboard<>();
        Assert.assertEquals(scoreboard.getScores().size(), 0, "Scoreboard isn't empty");
        Assert.assertEquals(scoreboard.getScore(player), 0.0, "Player shouldn't have a score");
        Assert.assertEquals(scoreboard.getScores().size(), 0, "Scoreboard changes after getting the score of a player");
        scoreboard.stream().forEach(i -> Assert.assertTrue(false, "Scoreboard stream contains a player"));
        scoreboard.clear();
        Assert.assertEquals(scoreboard.getPlayerHistory(player).size(), 0, "Player shouldn't have a history");
        Assert.assertEquals(scoreboard.getPlayersRanked().size(), 0, "There should be no players");
        scoreboard.toString();
    }

    @Test
    public void addPlayer(){
        Scoreboard<Object> scoreboard = new Scoreboard<>();
        scoreboard.addScore(player, 20);
        Assert.assertEquals(scoreboard.getScore(player), 20.0, "Score not updated");
        Assert.assertEquals(scoreboard.getPlayersRanked().size(), 1, "Scoreboard not storing players");
        Assert.assertTrue(scoreboard.getPlayersRanked().contains(player), "Players don't match");
        scoreboard.toString();
        scoreboard.addScore(alternate, 50);
        Assert.assertEquals(scoreboard.getPlayersRanked().size(), 2, "Scoreboard not storing multiple players");
        Assert.assertEquals(scoreboard.getScore(alternate), 50.0, "Scoreboard not updating second player correctly");
        Assert.assertEquals(scoreboard.getScore(player), 20.0, "Scoreboard incorrectly updating first player when adding second");
        Object topPlayer = scoreboard.getPlayersRanked().get(0);
        Assert.assertNotNull(topPlayer, "Scoreboard returning null players");
        Assert.assertEquals(topPlayer, alternate, "Scoring order not being respected");
        Scoreboard<Object> minScoreboard = new Scoreboard<>(false);
        minScoreboard.addScore(player, 20);
        minScoreboard.addScore(alternate, 50);
        Assert.assertEquals(minScoreboard.getPlayersRanked().get(0), player, "Min scoring not being respected");
    }

    @Test
    public void scoringMethods(){
        List<Pair<Object, Double>> scores = new ArrayList<>();
        scores.add(new Pair<>(player, 15.0));
        scores.add(new Pair<>(player, 50.0));
        scores.add(new Pair<>(player, 25.0));
        Scoreboard<Object> maxScoreboard = new Scoreboard<>(Scoreboard::maxAggregator);
        Scoreboard<Object> minScoreboard = new Scoreboard<>(Scoreboard::minAggregator);
        Scoreboard<Object> averageScoreboard = new Scoreboard<>(Scoreboard::meanAggregator);
        Scoreboard<Object> summingScoreboard = new Scoreboard<>(Scoreboard::sumAggregator);

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
