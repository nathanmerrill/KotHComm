package game.tournaments;

import game.PlayerRanking;

interface Tournament<T> {
    PlayerRanking<T> run();
}