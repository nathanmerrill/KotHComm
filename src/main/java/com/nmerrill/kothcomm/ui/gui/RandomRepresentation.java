package com.nmerrill.kothcomm.ui.gui;

import javafx.scene.paint.Color;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.multimap.MutableMultimap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.tuple.Tuples;

import java.util.Random;

import static javafx.scene.paint.Color.hsb;

public class RandomRepresentation<U> implements Representation<U>{
    private final static int FIRST_CHAR = '!', LAST_CHAR = '~';

    private final MutableMultimap<Character, Color> used;
    private final MutableMap<U, Pair<Character, Color>> cache;
    private final Random random;
    private Character current;

    public RandomRepresentation(){
        used = Multimaps.mutable.set.empty();
        random = new Random();
        current = FIRST_CHAR;
        cache = Maps.mutable.empty();
    }

    @Override
    public Pair<Character, Color> represent(U item) {
        if (cache.containsKey(item)){
            return cache.get(item);
        }
        Character c = fromObject(item);
        Color color = randomColor();
        while (used.containsKeyAndValue(c, color)){
            if (used.get(c).size() > 10) {
                c = nextCharacter();
            }
            color = randomColor();
        }

        Pair<Character, Color> pair = Tuples.pair(c, color);
        cache.put(item, pair);
        return pair;
    }

    private Character fromObject(U object){
        String toString = object.toString();
        Character first;
        if (toString.isEmpty()){
            first = nextCharacter();
        } else {
            first = toString.charAt(0);
        }
        return first;
    }

    private Character nextCharacter(){
        char ret = current;
        if (ret == LAST_CHAR){
            current = FIRST_CHAR;
        } else {
            current = (char)(ret + 1);
        }
        return ret;
    }

    private Color randomColor(){
        return hsb(random.nextDouble()*360, random.nextDouble(), random.nextDouble()*.8);
    }
}
