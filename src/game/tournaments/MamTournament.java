package game.tournaments;

import game.*;
import utils.iterables.CombinationIterable;
import utils.iterables.Pair;
import utils.iterables.Tools;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MamTournament<T> implements Tournament<T>{

    private final GameManager<T> gameManager;
    private final Directory<T> directory;
    private final int numIterations;

    public MamTournament(GameManager<T> gameManager, int numIterations){
        this.gameManager = gameManager;
        this.directory = gameManager.getDirectory();
        this.numIterations = numIterations;
    }

    @Override
    public PlayerRanking<T> run() {
        List<PlayerType<T>> players = this.directory.allPlayers();
        List<List<PlayerType<T>>> games = IntStream.range(0, numIterations)
                .mapToObj(i -> players)
                .map(Tools.applied(Collections::shuffle))
                .map(i -> i.subList(0, gameManager.preferredPlayerCount()))
                .collect(Collectors.toList());

        List<Scoreboard<T>> votes = gameManager.runGames(games);

        Map<Pair<PlayerType<T>, PlayerType<T>>, Integer> votesPreferences = new HashMap<>();
        SortedSet<Pair<PlayerType<T>, PlayerType<T>>> majorities = new TreeSet<>(new ImportanceComparator(votesPreferences));

        for (List<PlayerType<T>> playerPair: new CombinationIterable<>(players, 2)) {
            int p1Preferred = getPreferenceCount(votes, playerPair.get(0), playerPair.get(1));
            int p2Preferred = getPreferenceCount(votes, playerPair.get(1), playerPair.get(0));
            if (p1Preferred > p2Preferred) {
                majorities.add(new Pair<>(playerPair.get(0), playerPair.get(1)));
            } else if (p2Preferred > p1Preferred) {
                majorities.add(new Pair<>(playerPair.get(1), playerPair.get(0)));
            }
        }

        Set<Pair<PlayerType<T>, PlayerType<T>>> affirmations = new HashSet<>();
        for (Pair<PlayerType<T>, PlayerType<T>> majority: majorities) {
            affirm(majority, affirmations);
        }
        List<PlayerType<T>> topCandidates = players.stream()
                .filter(player ->
                        players.stream().noneMatch(
                                i ->affirmations.contains(new Pair<>(i, player))))
                .collect(Collectors.toList());
        Collections.shuffle(topCandidates);
        PlayerRanking<T> ranking = new PlayerRanking<>(players);
        topCandidates.forEach(ranking::rankTop);
        return ranking;
    }



    private void affirm(Pair<PlayerType<T>, PlayerType<T>> preference, Set<Pair<PlayerType<T>, PlayerType<T>>> affirmations) {
        affirmations.add(preference);
        for (PlayerType<T> player: directory.allPlayers().stream()
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
    class ImportanceComparator implements Comparator<Pair<PlayerType<T>, PlayerType<T>>> {
        private final Map<Pair<PlayerType<T>, PlayerType<T>>, Integer> preferences;
        private ImportanceComparator(Map<Pair<PlayerType<T>, PlayerType<T>>, Integer> preferences) {
            this.preferences = preferences;
        }

        @Override
        public int compare(Pair<PlayerType<T>, PlayerType<T>> p0, Pair<PlayerType<T>, PlayerType<T>> p1) {
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
    private int getPreferenceCount(List<Scoreboard<T>> votes, PlayerType<T> player1, PlayerType<T> player2) {
        return (int)votes.stream().filter(i -> i.getAggregatedScore(player1) > i.getAggregatedScore(player2)).count();
    }
}