package game.tournaments.types;

import game.*;
import game.tournaments.GameRanker;
import game.tournaments.RankerSupplier;
import utils.Pair;
import utils.iterables.PermutationIterable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MamRanker<T extends AbstractPlayer<T>> implements RankerSupplier<T> {

    private final GameManager<T> manager;

    public MamRanker(GameManager<T> gameManager){
        this.manager = gameManager;
    }

    @Override
    public Mam getRanker() {
        return new Mam();
    }

    private class Mam implements GameRanker<T> {
        private final List<PlayerType<T>> players;
        private final List<Map<PlayerType<T>, Double>> votes;

        public Mam(){
            players = manager.allPlayers();
            votes = new ArrayList<>();
        }

        @Override
        public void scoreGame(AbstractGame<T> game) {
            game.onFinish(scoreboard-> votes.add(
                    scoreboard.stream()
                            .collect(Collectors.toMap(
                                    AbstractPlayer::getType,
                                    scoreboard::getScore)
                            )
                    ));
        }

        @Override
        public Scoreboard<PlayerType<T>> getRankings() {
            Map<Pair<PlayerType<T>, PlayerType<T>>, Integer> votesPreferences =
                    new PermutationIterable<>(players, 2).stream()
                    .map(Pair::fromList)
                    .collect(Collectors.toMap(Function.identity(), this::getPreferenceCount));

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

            Scoreboard<PlayerType<T>> scoreboard = new Scoreboard<>(Scoreboard::sumAggregator, false);

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
                return manager.getRandom().nextInt(2)*2-1;
            }
        }
        private int getPreferenceCount(Pair<PlayerType<T>, PlayerType<T>> players) {
            return (int)votes.stream().filter(i -> i.get(players.first()) > i.get(players.second())).count();
        }

    }


}