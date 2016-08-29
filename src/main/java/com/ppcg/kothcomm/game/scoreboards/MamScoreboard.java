package com.ppcg.kothcomm.game.scoreboards;

import com.ppcg.kothcomm.utils.Pair;
import com.ppcg.kothcomm.utils.iterables.PermutationIterable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MamScoreboard<T> extends Scoreboard<T> {

    private final List<Map<T, Double>> votes;
    private final Map<T, Double> cache;
    private boolean cacheClean;
    private final Random random;

    public MamScoreboard(Random random){
        this.random = random;
        votes = new ArrayList<>();
        cache = new HashMap<>();
        cacheClean = false;
    }

    public MamScoreboard() {
        this(new Random());
    }

    @Override
    public void clearScores() {
        cacheClean = false;
        votes.clear();
    }

    @Override
    public void update(Map<T, Double> scores) {
        cacheClean = false;
        votes.add(new HashMap<>(scores));
    }

    @Override
    public double getScore(T item) {
        if (cacheClean){
            return cache.get(item);
        }
        List<T> rankings = rank();
        for (int i = 0; i < rankings.size(); i++){
            cache.put(rankings.get(i), (double)i);
        }
        cacheClean = true;
        return cache.get(item);
    }

    public List<T> rank() {
        Map<Pair<T, T>, Integer> votesPreferences =
                new PermutationIterable<>(items(), 2).stream()
                        .map(Pair::fromList)
                        .collect(Collectors.toMap(Function.identity(), this::getPreferenceCount));

        SortedSet<Pair<T, T>> majorities = new TreeSet<>(new ImportanceComparator(votesPreferences));
        new PermutationIterable<>(items(), 2).stream()
                .map(Pair::fromList)
                .filter((a) -> votesPreferences.get(a) > votesPreferences.get(a.swap()))
                .forEach(majorities::add);

        Set<Pair<T, T>> affirmations = new HashSet<>();
        for (Pair<T, T> majority : majorities) {
            affirm(majority, affirmations);
        }

        List<T> sortedCandidates = new ArrayList<>(items());
        Collections.sort(sortedCandidates, (o1, o2) -> {
            if (affirmations.contains(new Pair<>(o1, o2))) {
                return -1;
            } else if (affirmations.contains(new Pair<>(o2, o1))) {
                return 1;
            }
            return 0;
        });
        return sortedCandidates;
    }

    private void affirm(Pair<T, T> preference, Set<Pair<T, T>> affirmations) {
        affirmations.add(preference);
        for (T player : items().stream()
                .filter(p ->
                        !p.equals(preference.first()) &&
                                !p.equals(preference.second()))
                .collect(Collectors.toList())) {
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

    class ImportanceComparator implements Comparator<Pair<T, T>> {
        private final Map<Pair<T, T>, Integer> preferences;

        private ImportanceComparator(Map<Pair<T, T>, Integer> preferences) {
            this.preferences = preferences;
        }

        @Override
        public int compare(Pair<T, T> p0, Pair<T, T> p1) {
            if (p0 == null || p1 == null) {
                throw new NullPointerException();
            }
            if (p0.equals(p1)) {
                return 0;
            }
            int preferred = preferences.get(p0) - preferences.get(p1);
            if (preferred != 0) {
                return preferred;
            }
            int opposition = preferences.get(p1.swap()) - preferences.get(p0.swap());
            if (opposition != 0) {
                return opposition;
            }
            return random.nextInt(2) * 2 - 1;
        }
    }

    private int getPreferenceCount(Pair<T, T> players) {
        return (int) votes.stream().filter(i -> i.get(players.first()) > i.get(players.second())).count();
    }


}
