package com.ppcg.kothcomm.game.maps.generators;

import com.ppcg.kothcomm.game.maps.GameMap;

public interface Generator<U extends GameMap> {
    void generate(U map);
}
