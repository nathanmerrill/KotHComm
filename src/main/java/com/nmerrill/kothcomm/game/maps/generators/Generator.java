package com.nmerrill.kothcomm.game.maps.generators;

import com.nmerrill.kothcomm.game.maps.GameMap;

public interface Generator<U extends GameMap> {
    void generate(U map);
}
