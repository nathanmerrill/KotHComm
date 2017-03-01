# KotHComm

This is a framework to help you build King of the Hill challenges, which pits bots against each other in a game designed by you.

Here's a list of all of the components of KoTHComm:

**Game**:  This is where the magic happens: A single game that pits players against each other.

**Player**:  You will make a player class, that other submissions will implement.  We'll pass the players to your games.

**Tournament**:  A tournament decides which players go into each game.  By default, `Sampling` is chosen (chooses them at random, ensuring all players are chosen an equal number of times)

**Aggregator**:  An aggregator takes all of the scores, and figures out how to rank the players from the scores.  We use the average score by default.

**Arguments**:  We support a large amount of runtime arguments, such as the number of games to run, or a random seed to use.  This class contains all of them, and you can extend it to support more arguments.

**Downloader**:  We can automatically download submisssions from StackExchange, if you pass the question id in as an argument.  Each submission needs to have their file name on the first line of their code block

**Maps**:  An assorted collection of maps and map generators are provided

**Player loader**:  If you do use the downloader, we can automatically identify and load players.  If you use the `PipeLoader`, we can also communicate with submissions in any language, assuming they provide a `command.txt` giving commands on how to start them up.

**UI**:  A couple of ui components are given to allow control over KoTHComm.  Still a work in progress.

#How to use:

##Setup

I highly recommend using [Gradle](https://gradle.org/) to build your project.  If you do, you can use KoTHComm as a library by adding the following to your build.gradle:

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    compile 'com.github.nathanmerrill:KoTHComm:1.0.1'
}
```

If you don't want to use Gradle, you have 2 other options:

1. Use KoTHComm as a Git submodule by cloning it into your project directory
2. Download the [latest release JAR](https://github.com/nathanmerrill/KotHComm/releases), and use it as a library
 
##Write some code

- Make a player class.  It should extend from AbstractPlayer.
```java
import com.nmerrill.kothcomm.game.players.AbstractPlayer;
public abstract class TestPlayer extends AbstractPlayer<TestPlayer> {
      //This is the public API for submissions.  Put the methods that they will need to implement here
}
```
- Make a game class.  It should extend from AbstractGame.  If your game has a fixed number of iterations, you can use IteratedGame.  If your game will run off of an Action queue, you can use ActionQueueGame or MaxActionQueueGame.
```java
import com.nmerrill.kothcomm.game.scoring.Scoreboard;

public class TestGame extends AbstractGame<TestPlayer> {
    protected void setup() {
         //This method is called before your game starts.  
         //Your random variable will be initialized, and use the players variable to access the players in the game
    }

    @Override
    public Scoreboard<TestPlayer> getScores() {
        //You should return the current scores of all of the players
        //Ensure that the scores are as meaningful as possible, so that more data can be used to rank the players
        return null;
    }

    @Override
    public boolean finished() {
        // You Should return true if the game is finished
        return false;
    }

    @Override
    protected void step() {
        //Step will be called repeatedly until finished() returns true.
        //This is where the actual game logic goes
    }
}
```
- Make a main class:
```java
import com.nmerrill.kothcomm.game.KotHComm;

public class Main {
    public static void main(String[] args){
        KotHComm<TestPlayer, TestGame> kotHComm = new KotHComm<>(TestGame::new);
        //KotHComm is highly customizable. A large number of methods on kotHComm allow you to set how you want your KotH to run.
        kotHComm.run(args);
    }
}
```

Congrats, you've started your first King of the Hill challenge!
