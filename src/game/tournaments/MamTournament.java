package game.tournaments;

import game.Game;
import game.Player;
import game.PlayerRanking;
import game.Scoreboard;
import utils.iterables.CombinationIterable;
import utils.iterables.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MamTournament implements Tournament{

    private final Function<List<Player>, Game> gameFactory;
    private final List<Player> players;
    private final int numIterations;
    private final Random random;

    public MamTournament(Function<List<Player>, Game> gameFactory, List<Player> players, int numIterations){
        this.gameFactory = gameFactory;
        this.players = players;
        this.numIterations = numIterations;
        this.random = new Random();
    }

    @Override
    public PlayerRanking run() {
        List<Scoreboard> votes = IntStream.range(0,numIterations)
                .mapToObj(i -> gameFactory.apply(players).run())
                .collect(Collectors.toList());

        Map<Pair<Player, Player>, Integer> votesPreferences = new HashMap<>();
        SortedSet<Pair<Player, Player>> majorities = new TreeSet<>(new ImportanceComparator(votesPreferences));

        for (List<Player> playerPair: new CombinationIterable<>(players, 2)) {
            int p1Preferred = getPreferenceCount(votes, playerPair.get(0), playerPair.get(1));
            int p2Preferred = getPreferenceCount(votes, playerPair.get(1), playerPair.get(0));
            if (p1Preferred > p2Preferred) {
                majorities.add(new Pair<>(playerPair.get(0), playerPair.get(1)));
            } else if (p2Preferred > p1Preferred) {
                majorities.add(new Pair<>(playerPair.get(1), playerPair.get(0)));
            }
        }

        Set<Pair<Player, Player>> affirmations = new HashSet<>();
        for (Pair<Player, Player> majority: majorities) {
            affirm(majority, affirmations);
        }
        List<Player> topCandidates = players.stream()
                .filter(player ->
                        players.stream().noneMatch(
                                i ->affirmations.contains(new Pair<>(i, player))))
                .collect(Collectors.toList());
        Collections.shuffle(topCandidates, random);
        PlayerRanking ranking = new PlayerRanking(players);
        topCandidates.forEach(ranking::rankTop);
        return ranking;
    }
    private void affirm(Pair<Player, Player> preference, Set<Pair<Player, Player>> affirmations) {
        affirmations.add(preference);
        for (Player player: players.stream()
                .filter(p ->
                        !p.equals(preference.first()) &&
                        !p.equals(preference.second()))
                .collect(Collectors.toList())){
            if (affirmations.contains(new Pair<>(player, preference.first())) &&
                    !affirmations.contains(new Pair<>(player, preference.second()))) {
                affirm(new Pair<>(player, preference.second()), affirmations);
            }
            if (affirmations.contains(new Pair<>(preference.second(), player)) &&
                    !affirmations.contains(new Pair<>(preference.first(), player))) {
                affirm(new Pair<>(preference.first(), player), affirmations);
            }
        }
    }
    class ImportanceComparator implements Comparator<Pair<Player, Player>> {
        private final Map<Pair<Player, Player>, Integer> preferences;
        private ImportanceComparator(Map<Pair<Player, Player>, Integer> preferences) {
            this.preferences = preferences;
        }

        @Override
        public int compare(Pair<Player, Player> p0, Pair<Player, Player> p1) {
            if (p0 == null || p1 == null){
                throw new NullPointerException();
            }
            int preferred = preferences.get(p0) - preferences.get(p1);
            if (preferred != 0){
                return preferred;
            }
            int opposition = preferences.get(new Pair<>(p1.second(), p1.first()))
                    - preferences.get(new Pair<>(p0.second(), p0.first()));
            if (opposition != 0){
                return opposition;
            }
            return 0;
        }
    }
    private int getPreferenceCount(List<Scoreboard> votes, Player player1, Player player2) {
        return (int)votes.stream().filter(i -> i.getAggregatedScore(player1) > i.getAggregatedScore(player2)).count();
    }
}