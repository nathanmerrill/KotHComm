package utils.iterables;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class PermutationIterable<T> extends SubsequenceIterable<T> {

    private final SortedSet<Integer> availableDigits;

    public PermutationIterable(Iterator<T> iter, int length) {
        super(iter, length);
        this.availableDigits = new TreeSet<>(Tools.range(pool.size()).subList(length, pool.size()));
    }
    public PermutationIterable(Iterator<T> iter){
        this(iter, 2);
    }
    public PermutationIterable(Iterable<T> iter){
        this(iter.iterator());
    }
    public PermutationIterable(Iterable<T> iter, int length){
        this(iter.iterator(), length);
    }

    protected void nextDigits(int index) {
        if (index < 0) {
            hasNext = false;
            return;
        }
        int digit = digits.get(index);
        SortedSet<Integer> nextAvailable = new TreeSet<>(availableDigits.tailSet(digit));
        availableDigits.add(digit);
        int nextDigit;
        if (nextAvailable.isEmpty()) {
            nextDigits(index - 1);
            nextDigit = availableDigits.first();
        } else {
            nextDigit = nextAvailable.first();
        }
        digits.set(index, nextDigit);
        availableDigits.remove(nextDigit);
    }
}
