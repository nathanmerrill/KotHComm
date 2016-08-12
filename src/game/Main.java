package game;


import game.bots.DumbBot;
import game.bots.PipeBot;
import game.tournaments.TournamentRunner;
import game.tournaments.types.EloTournament;


public class Main {

    public static void main(String[] args){
        GameManager<Player> manager = new GameManager<>(StockExchange::new).playerCount(StockExchange.NUM_STOCKS);
        manager.registerDirectory(PipeBot::new, "src/game/bots");
        manager.register("DumbBot", DumbBot::new);
        manager.register("DumbBot2", DumbBot::new);
        manager.register("DumbBot3", DumbBot::new);
        manager.register("DumbBot4", DumbBot::new);
        manager.register("DumbBot5", DumbBot::new);


        TournamentRunner<Player> runner = new TournamentRunner<>(new EloTournament<>(manager));
        System.out.print(runner.run(50));

    }
}
