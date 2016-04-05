package game.tournaments;

import game.PlayerType;

interface Tournament<T> {
    Iterable<PlayerType<T>> run();
}