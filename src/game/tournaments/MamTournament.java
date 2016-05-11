package game.tournaments;

import game.*;
import game.exceptions.InvalidPlayerCountException;
import utils.iterables.CombinationIterable;
import utils.iterables.Pair;
import utils.iterables.PermutationIterable;
import utils.iterables.Tools;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class MamTournament<T> implements Tournament<T>{

    private final GameManager<T> manager;
    private final int numIterations;

    public MamTournament(GameManager<T> gameManager, int numIterations){
        this.manager = gameManager;
        this.numIterations = numIterations;
    }


    @Override
    public MamTournamentIterator iterator() {
        return new MamTournamentIterator();
    }
    private class MamTournamentIterator implements TournamentIterator<T>{
        private final List<PlayerType<T>> players;
        private final List<Scoreboard<T>> votes;
        private final Iterator<Game<T>> games;

        public MamTournamentIterator(){
            players = manager.getDirectory().allPlayers();
            if (players.size() < manager.minPlayerCount()){
                throw new InvalidPlayerCountException("Need more players");
            }
            int gameSize = Math.min(players.size(), manager.preferredPlayerCount());
            votes = new ArrayList<>();
            games = IntStream.range(0, numIterations)
                    .mapToObj(i -> players)
                    .map(Tools.applied(Collections::shuffle))
                    .map(i -> i.subList(0, gameSize))
                    .map(manager::construct)
                    .collect(Collectors.toList()).iterator();


        }

        @Override
        public Game<T> next() {
            Game<T> next = games.next();
            next.onFinish(votes::add);
            return next;
        }

        @Override
        public boolean hasNext() {
            return games.hasNext();
        }

        @Override
        public Scoreboard<T> currentRankings() {
            Map<Pair<PlayerType<T>, PlayerType<T>>, Integer> votesPreferences =
                    new PermutationIterable<>(players, 2).stream()
                    .map(Pair::fromList)
                    .collect(Collectors.toMap(Function.identity(), (a) -> getPreferenceCount(votes, a)));

            SortedSet<Pair<PlayerType<T>, PlayerType<T>>> majorities = new TreeSet<>(new ImportanceComparator(votesPreferences));
            new PermutationIterable<>(players, 2).stream()
                    .map(Pair::fromList)
                    .filter((a) -> votesPreferences.get(a) > votesPreferences.get(a.swap()))
                    .forEach(majorities::add);

            Set<Pair<PlayerType<T>, PlayerType<T>>> affirmations = new HashSet<>();
            for (Pair<PlayerType<T>, PlayerType<T>> majority: majorities) {
                affirm(majority, affirmations);
            }

            List<PlayerType<T>> sortedCandidates = new ArrayList<>(players);
            Collections.sort(sortedCandidates, (o1, o2) -> {
                if (affirmations.contains(new Pair<>(o1, o2))){
                    return -1;
                } else if (affirmations.contains(new Pair<>(o2, o1))){
                    return 1;
                }
                return 0;
            });

            Scoreboard<T> scoreboard = new Scoreboard<>(Scoreboard::sumAggregator, false);

            int currentRank = 1;

            for (int i = 0; i < sortedCandidates.size(); i++){
                scoreboard.addScore(sortedCandidates.get(i), currentRank);
                if (i + 1 < sortedCandidates.size()) {
                    if (affirmations.contains(new Pair<>(sortedCandidates.get(i), sortedCandidates.get(i + 1)))) {
                        currentRank++;
                    }
                }
            }
            return scoreboard;
        }

        private void affirm(Pair<PlayerType<T>, PlayerType<T>> preference, Set<Pair<PlayerType<T>, PlayerType<T>>> affirmations) {
            affirmations.add(preference);
            for (PlayerType<T> player: players.stream()
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
                if (p0.equals(p1)){
                    return 0;
                }
                int preferred = preferences.get(p0) - preferences.get(p1);
                if (preferred != 0){
                    return preferred;
                }
                int opposition = preferences.get(p1.swap()) - preferences.get(p0.swap());
                if (opposition != 0){
                    return opposition;
                }
                return new Random().nextInt();
            }
        }
        private int getPreferenceCount(List<Scoreboard<T>> votes, Pair<PlayerType<T>, PlayerType<T>> players) {
            return (int)votes.stream().filter(i -> i.getAggregatedScore(players.first()) > i.getAggregatedScore(players.second())).count();
        }

    }


}