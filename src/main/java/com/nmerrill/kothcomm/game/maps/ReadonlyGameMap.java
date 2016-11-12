package com.nmerrill.kothcomm.game.maps;

import org.eclipse.collections.api.map.MutableMap;


public class ReadonlyGameMap<U extends MapPoint, T>  {
    private final GameMap<U, T> map;
    public ReadonlyGameMap(GameMap<U, T> map){
        this.map = map;
    }

    public T get(U point){
        return map.get(point);
    }

    public boolean contains(U point, T item){
        return map.contains(point, item);
    }

    public boolean contains(U point){
        return map.contains(point);
    }

    public boolean outOfBounds(U point){
        return map.outOfBounds(point);
    }

    public MutableMap<U, T> toMap(){
        return map.toMap();
    }

    public boolean inBounds(U point){
        return map.inBounds(point);
    }

}
