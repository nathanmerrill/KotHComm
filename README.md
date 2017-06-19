# KotHComm

This is a framework to help you build King of the Hill challenges, which pits bots against each other in a game designed by you.  It includes:

- Automatic downloading of submissions from StackExchange
- Support for submissions in any language capable of I/O
- Organize your games into different types of tournaments, and combine their scores using a variety of methods
- Maps and map generators
- Various GUI components that you can use in your JavaFX application

# Getting started:

## Setup

I highly recommend using [Gradle](https://gradle.org/) to build your project.  If you do, you can use KoTHComm as a library by adding the following to your build.gradle:

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    compile 'com.github.nathanmerrill:KoTHComm:1.0.10' //Whatever the latest version is here: https://github.com/nathanmerrill/KotHComm/releases
}
```

If you don't want to use Gradle, you can also:

1. Use Maven to manage your project (you'll need to add jitpack as a repository, and this project as a dependency)
2. Download the [latest release JAR](https://github.com/nathanmerrill/KotHComm/releases), and use it as a library
3. Use KoTHComm as a Git submodule by cloning it into your project directory
 
## Write some code

- Make a player class.  It should extend from AbstractPlayer.
```java
import com.nmerrill.kothcomm.game.players.AbstractPlayer;
public abstract class TestPlayer extends AbstractPlayer<TestPlayer> {
      //This is the public API for submissions.  Put the methods that they will need to implement here
}
```
- Make a game class.  It should extend from AbstractGame (or any class in the [game.games package](https://github.com/nathanmerrill/KotHComm/tree/master/src/main/java/com/nmerrill/kothcomm/game/games).
```java
import com.nmerrill.kothcomm.game.scoring.Scoreboard;

public class TestGame extends AbstractGame<TestPlayer> {

    public TestGame(){
        //Do not use the players or random variable here.  They are not instantiated yet
    }
    
    protected void setup() {
         //This method is called before your game starts.  
         //Your random variable will be initialized, and use the players variable to access the players in the game
    }

    @Override
    public Scoreboard<TestPlayer> getScores() {
        //You should return the current scores of all of the players
        //Ensure that the scores are as meaningful as possible, so that more data can be used to rank the players
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

If you are looking to do something in particular, check the [HowTo document](HowTo.md)
