package utils.iterables;

import java.util.Iterator;

class ProductIterable<T> extends SubsequenceIterable<T> {

    public ProductIterable(Iterator<T> iter, int length) {
        super(iter, length);
    }

    public ProductIterable(Iterator<T> iter){
        this(iter, 2);
    }
    public ProductIterable(Iterable<T> iter){
        this(iter.iterator());
    }
    public ProductIterable(Iterable<T> iter, int length){
        this(iter.iterator(), length);
    }

    protected void nextDigits(int index) {
        if  (index < 0){
            hasNext = false;
            return;
        }
        int digit = digits.get(index);
        int maxDigit = pool.size() - 1;
        if (digit == maxDigit) {
            nextDigits(index -1);
            digits.set(index, 0);
        } else {
            digits.set(index, digit+1);
        }
    }

}
