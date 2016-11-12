package com.nmerrill.kothcomm.game.scoring;

import com.nmerrill.kothcomm.utils.iterables.Itertools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.sorted.MutableSortedSet;
import org.eclipse.collections.api.tuple.Twin;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.SortedSets;
import org.eclipse.collections.impl.tuple.Tuples;

import java.util.*;

public final class MamAggregator<T> implements Aggregator<Scoreboard<T>> {

    private final Random random;

    public MamAggregator(Random random){
        this.random = random;
    }

    public MamAggregator() {
        this(new Random());
    }


    @Override
    public Scoreboard<T> aggregate(MutableList<? extends Scoreboard<T>> scores) {
        MutableList<MutableSortedSet<T>> votes = scores.collect(Scoreboard::rank);
        MutableSet<T> players = votes.flatCollect(i -> i).toSet();
        MutableList<Twin<T>> playerPairs = Itertools.permutation(players).toList()
                .collect(l -> Tuples.twin(l.get(0), l.get(1)));

        MutableMap<Twin<T>, Integer> votesPreferences = playerPairs.toMap(i -> i, i -> getPreferenceCount(i, scores));

        MutableSortedSet<Twin<T>> majorities = SortedSets.mutable.ofAll(
                new ImportanceComparator(votesPreferences),
                playerPairs.select((a) -> votesPreferences.get(a) > votesPreferences.get(a.swap())));

        Set<Twin<T>> affirmations = new HashSet<>();
        majorities.forEach(t -> this.affirm(t, affirmations, players));

        return Scoreboard.toScoreboard(players.toSortedList((o1, o2) -> {
            Twin<T> pair = Tuples.twin(o1, o2);
            if (affirmations.contains(pair)) {
                return -1;
            } else if (affirmations.contains(pair.swap())) {
                return 1;
            }
            return 0;
        }));
    }

    private void affirm(Twin<T> preference, Set<Twin<T>> affirmations, MutableSet<T> players) {
        affirmations.add(preference);
        for (T player : players.withoutAll(Lists.fixedSize.of(preference.getOne(), preference.getTwo()))){
            Twin<T> one = Tuples.twin(player, preference.getOne());
            Twin<T> two = Tuples.twin(player, preference.getTwo());
            if (affirmations.contains(one) && !affirmations.contains(two)) {
                affirm(two, affirmations, players);
            }
            if (affirmations.contains(two.swap()) && !affirmations.contains(one.swap())) {
                affirm(one.swap(), affirmations, players);
            }
        }
    }

    private class ImportanceComparator implements Comparator<Twin<T>> {
        private final MutableMap<Twin<T>, Integer> preferences;

        private ImportanceComparator(MutableMap<Twin<T>, Integer> preferences) {
            this.preferences = preferences;
        }

        @Override
        public int compare(Twin<T> p0, Twin<T> p1) {
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

    private int getPreferenceCount(Twin<T> players, MutableList<? extends Scoreboard<T>> votes) {
        return votes.count(i -> i.compare(players.getOne(), players.getTwo()) < 0);
    }


}
