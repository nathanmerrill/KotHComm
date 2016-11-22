package com.nmerrill.kothcomm.ui.gui;

import javafx.scene.paint.Color;
import org.eclipse.collections.api.tuple.Pair;


public interface Representation<U> {
    Pair<Character, Color> represent(U item);
}
