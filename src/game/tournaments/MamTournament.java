package game.tournaments;

import game.*;
import utils.iterables.CombinationIterable;
import utils.iterables.Pair;
import utils.iterables.Tools;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MamTournament implements Tournament{

    private final GameManager gameManager;
    private final List<PlayerType> players;
    private final int numIterations;
    private final Random random;

    public MamTournament(GameManager gameManager, List<PlayerType> players, int numIterations){
        this.gameManager = gameManager;
        this.players = players;
        this.numIterations = numIterations;
        this.random = new Random();
    }

    @Override
    public PlayerRanking run() {
        List<PlayerType> players = new ArrayList<>(this.players);
        List<List<PlayerType>> games = IntStream.range(0, numIterations)
                .mapToObj(i -> players)
                .map(Tools.applied(Collections::shuffle))
                .map(i -> i.subList(0, gameManager.preferredPlayerCount()))
                .collect(Collectors.toList());

        List<Scoreboard> votes = gameManager.runGames(games);

        Map<Pair<PlayerType, PlayerType>, Integer> votesPreferences = new HashMap<>();
        SortedSet<Pair<PlayerType, PlayerType>> majorities = new TreeSet<>(new ImportanceComparator(votesPreferences));

        for (List<PlayerType> playerPair: new CombinationIterable<>(players, 2)) {
            int p1Preferred = getPreferenceCount(votes, playerPair.get(0), playerPair.get(1));
            int p2Preferred = getPreferenceCount(votes, playerPair.get(1), playerPair.get(0));
            if (p1Preferred > p2Preferred) {
                majorities.add(new Pair<>(playerPair.get(0), playerPair.get(1)));
            } else if (p2Preferred > p1Preferred) {
                majorities.add(new Pair<>(playerPair.get(1), playerPair.get(0)));
            }
        }

        Set<Pair<PlayerType, PlayerType>> affirmations = new HashSet<>();
        for (Pair<PlayerType, PlayerType> majority: majorities) {
            affirm(majority, affirmations);
        }
        List<PlayerType> topCandidates = players.stream()
                .filter(player ->
                        players.stream().noneMatch(
                                i ->affirmations.contains(new Pair<>(i, player))))
                .collect(Collectors.toList());
        Collections.shuffle(topCandidates, random);
        PlayerRanking ranking = new PlayerRanking(players);
        topCandidates.forEach(ranking::rankTop);
        return ranking;
    }



    private void affirm(Pair<PlayerType, PlayerType> preference, Set<Pair<PlayerType, PlayerType>> affirmations) {
        affirmations.add(preference);
        for (PlayerType player: players.stream()
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
    class ImportanceComparator implements Comparator<Pair<PlayerType, PlayerType>> {
        private final Map<Pair<PlayerType, PlayerType>, Integer> preferences;
        private ImportanceComparator(Map<Pair<PlayerType, PlayerType>, Integer> preferences) {
            this.preferences = preferences;
        }

        @Override
        public int compare(Pair<PlayerType, PlayerType> p0, Pair<PlayerType, PlayerType> p1) {
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
    private int getPreferenceCount(List<Scoreboard> votes, PlayerType player1, PlayerType player2) {
        return (int)votes.stream().filter(i -> i.getAggregatedScore(player1) > i.getAggregatedScore(player2)).count();
    }
}