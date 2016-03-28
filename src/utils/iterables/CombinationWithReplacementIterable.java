package utils.iterables;

import java.util.Iterator;

public class CombinationWithReplacementIterable<T> extends SubsequenceIterable<T> {

    public CombinationWithReplacementIterable(Iterator<T> iter, int length) {
        super(iter, length);
    }
    public CombinationWithReplacementIterable(Iterator<T> iter){
        this(iter, 2);
    }
    public CombinationWithReplacementIterable(Iterable<T> iter){
        this(iter.iterator());
    }
    public CombinationWithReplacementIterable(Iterable<T> iter, int length){
        this(iter.iterator(), length);
    }

    protected void nextDigits(int index) {
        if (index < 0) {
            return;
        }
        int digit = digits.get(index);
        int digitMax = index + 1 == digits.size() ? pool.size() - 1 : index + 1;
        if (digit > digitMax) {
            nextDigits(index - 1);
            digits.set(index, digits.get(index - 1));
        } else {
            digits.set(index, digit + 1);
        }
    }
}
