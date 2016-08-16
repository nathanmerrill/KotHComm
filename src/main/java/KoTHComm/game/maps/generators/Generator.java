package KoTHComm.game.maps.generators;

import KoTHComm.game.maps.GameMap;

public interface Generator<U extends GameMap> {
    void generate(U map);
}
