# KotHComm

This is a framework to help you build King of the Hill challenges, which pits bots against each other in a game designed by you.

A simple starting snippet using KoTHComm looks like:

    public static void main(String[] args){
        KoTHComm runner = new KoTHComm(YourGame::new);
        runner.addSubmission("Player One", SomeSubmission::new);
        runner.addSubmission("Player Two", SomeSubmission::new);
        runner.addSubmission("Player Three", SomeSubmission::new);
        runner.run(args);
    }

The above snippet will create and run 50 games of YourGame.  For each of the games, it'll pick a random two submissions, instantiate them, and pair them together.  Finally, it'll take all of the scores returned by your game, and aggregate them together, and print them out.

However, *all* of what I described is customizable.  Here's a list of all of the components of KoTHComm:

**Game**:  You need to write a game that extends `AbstractGame`.  It will be provided with a list of players, a source of randomness.  It has 3 important methods:  `setup()` is called at the start, `step()` is called each step of the game, and `getScores()` is called to get the current scoreboard for the game.

**Player**:  You need to write a class that extends `AbstractPlayer`.  Submissions will need to write a class that extends your player class.

 **Tournament**:  A tournament decides which players go into each game.  By default, `Sampling` is chosen.

**Aggregator**:  An aggregator takes all of the scores, and figures out how to rank the players from the scores.  We use average by default.

**Arguments**:  We have a current set of arguments that your challenge takes (which is why you pass in `args`).  You can extend that class to add more.

**Downloader**:  We can automatically download submisssions from StackExchange, if you pass in the question id.  Each submission needs to have their file name on the first line of their multi-line code block

**Maps**:  A bunch of 2D and other various maps are provided.

**Player loader**:  If you do use the downloader, we can automatically identify and load players.  If you use the `PipeLoader`, we can also communicate with submissions in any language, assuming they provide a `command.txt` giving commands on how to start them up.

**UI**:  A couple of ui components are given to allow control over KoTHComm.  Still a work in progress.
