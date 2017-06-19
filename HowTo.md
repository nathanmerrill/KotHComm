# How do I:

- Allow submission of other arbitrary languages:
  - Call `kothComm.addLanguage(new OtherLoader(YourPipePlayer::new))`.  YourPipePlayer is a class you need to make that implements your Player class.  The constructor needs to take a PipeCommuicator, and you will use that communicator to send messages in each of the functions.  [Example](https://github.com/nathanmerrill/StockExchange/blob/master/src/main/java/com/ppcg/stockexchange/PipeBot.java)

- Download submissions from StackExchange:
  - When you run your program, pass in `-q 012345`, replacing the number with the question ID of the stack exchange post.  Submission posts should:
    - Have a header file with their name, then a comma, then the language they are written in
    - Put their submission in a multi-line code block.  The first line should be the file name, and Java submissions should not have a package declaration
    - Multiple multi-line code blocks are fine
    - Other languages should include a `command.txt` which contains commands to execute the submission

- Manually add a submission:
  - If they are Java submission in your classpath, call `kothComm.addSubmission("submissionName", YourSubmission::new)`
  - If they are any other language, put them in the folder `submissions/other`, add a command.txt, and add the OtherLoader (as listed above)
  
- Change the number of games:
  - Pass in `-i 500` when you are running your program
  - If you want to change the default, create a new arguments: `Arguments arguments = new Arguments()`, then set `arguments.iterations = 500`


- Change the number of players per game:
  - By default, we put 2 players into each game.  You can call `kothComm.setGameSize(10)` to change that
  
- Make my tournament deterministic:
  - Ensure that your game class and all submissions are using the random variable passed to them.
  - Ensure that other language submission are being passed a random seed by overriding `setRandom()` in your PipePlayer class.
  - Pass in `-r 012345` to set the random seed

- Change how players get put into games:
  - You want to change the tournament type.  We have several built-in in the [game.tournaments](https://github.com/nathanmerrill/KotHComm/tree/master/src/main/java/com/nmerrill/kothcomm/game/tournaments) package, but you can build your own if you'd like.  Some common ones are:
    - [Sampling](https://github.com/nathanmerrill/KotHComm/blob/master/src/main/java/com/nmerrill/kothcomm/game/tournaments/Sampling.java) tournaments pick the submission at random while ensuring that everybody plays the same number of games.
    - [Round Robin](https://github.com/nathanmerrill/KotHComm/blob/master/src/main/java/com/nmerrill/kothcomm/game/tournaments/RoundRobin.java) tournaments iterate through every possible combination of submissions
    - [Adjacent Player](https://github.com/nathanmerrill/KotHComm/blob/master/src/main/java/com/nmerrill/kothcomm/game/tournaments/AdjacentPlayer.java) tournaments select a submission at random, then adds other submissions with the closest score
  - After selecting your tournament type, call `kothComm.setTournament(YourTournament::new)`

- Change how the scores are added up:
  - You want to change the Aggregator for scoreboards.   We have several built-in aggregators in the [game.scoring](https://github.com/nathanmerrill/KotHComm/tree/master/src/main/java/com/nmerrill/kothcomm/game/scoring) package, but you can build your own if you'd like.  Some common ones are:
    - [ItemAggregator](https://github.com/nathanmerrill/KotHComm/blob/master/src/main/java/com/nmerrill/kothcomm/game/scoring/ItemAggregator.java) is the simplest:  It takes all of the scores for a particular submission, and averages them.  If you don't want the average, you can pass any other function you'd like (such as sum, min, max, or median).
    - [ScoredRankingsAggregator](https://github.com/nathanmerrill/KotHComm/blob/master/src/main/java/com/nmerrill/kothcomm/game/scoring/ScoredRankingsAggregator.java) allows you to set a fixed number of points for wins, ties, and losses, and sums them up.
    - [EloAggregator](https://github.com/nathanmerrill/KotHComm/blob/master/src/main/java/com/nmerrill/kothcomm/game/scoring/EloAggregator.java) calculates the ELO for each player, and updates the score based off of the ELO formula.
  - After selected the aggregator you want, call `kothComm.setAggregator(new YourAggregator())`

- Add some run-time arguments you want to parse:
  - Extend the Arguments class, then call `kothComm.setArgumentParser(new YourArguments())`


- Create a GUI:
  - Your Main class should extend JavaFX's Application class.
  - In your main function, parse your arguments and test to see if `useGui` is true.  If so, call `launch(this)`.
  - If you want your GUI to support running lots of games (even simultaneously), add `TournamentPane` to the root pane.
  - If you want your GUI to support viewing a game (with start/stop buttons), pass in a GamePane to the TournamentPane above (or to the root pane if you don't have a TournamentPane)
  - Create your own game view, and add it as a child to the GamePane.
  - After you've created everything, call your program with `-g true`
  
- Use a map:
  - There are 2 "typical" maps:  
    - The GraphGraphMap (I hate the name too) pairs locations to values.  If you want two locations to be connected, you need to manually connect them.
    - The NeighborhoodGraphMap takes a Neighborhood (lists relative connections, such as HexagonalNeighborhood or MooreNeighborhood), and a Region (the boundaries of the map), and determines connections using them.  This is less flexible (you can't just connect two arbitrary points), but it means we don't have to store connections
  - If your map isn't a GraphMap (it can't be mapped to a graph, e.g.: uses floating point numbers for position), then you'll want to extend from GameMap
  - You can use a [Generator](https://github.com/nathanmerrill/KotHComm/tree/master/src/main/java/com/nmerrill/kothcomm/game/maps/generators) to populate the contents of your map.
