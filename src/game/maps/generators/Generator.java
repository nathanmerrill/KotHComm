package game.maps.generators;

import game.maps.GameMap;

public interface Generator<U extends GameMap> {
    void generate(U map);
}
