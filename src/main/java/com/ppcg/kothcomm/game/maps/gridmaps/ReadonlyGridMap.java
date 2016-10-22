package com.ppcg.kothcomm.game.maps.gridmaps;


import com.ppcg.kothcomm.game.maps.MapPoint;
import com.ppcg.kothcomm.game.maps.PointOutOfBoundsException;
import com.ppcg.kothcomm.game.maps.ReadonlyGameMap;
import com.ppcg.kothcomm.game.maps.gridmaps.bounds.Bounds;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ReadonlyGridMap<U extends MapPoint, T> extends ReadonlyGameMap<U, T>, Iterable<U>{

    MutableSet<U> getNeighbors(U origin);
    MutableSet<U> locations();
    ReadonlyGridMap<U, T> subMap(Bounds<U> bounds);
    U randomPoint(boolean allowEmpty);
    MutableList<T> items();

    default MutableSet<U> emptyLocations(){
        return locations().select(this::isEmpty);
    }

    default MutableSet<U> filledLocations(){
        return locations().select(this::isFilled);
    }

    default boolean isFilled(U point){
        return !isEmpty(point);
    }

    default boolean isEmpty(U point){
        if (outOfBounds(point)){
            throw new PointOutOfBoundsException();
        }
        return get(point) == null;
    }

    default boolean isNeighbor(U origin, U neighbor){
        return getNeighbors(origin).contains(neighbor);
    }

    /**
     * @param from Start point
     * @param to End point
     * @return True if there is a path between the two points, false otherwise.
     */
    default boolean isPath(U from, U to){
        return shortestPath(from, to).isEmpty();
    }

    /**
     * @param from Start point
     * @param to End point
     * @return The shortest path between the two points.  If the points are not connected, then an empty list will be returned.
     */
    default MutableList<U> shortestPath(U from, U to){

        MutableMap<U, ImmutableList<U>> paths = Maps.mutable.of(from, Lists.immutable.empty());
        Queue<U> toVisit = new ArrayDeque<>();
        toVisit.add(from);
        while (!toVisit.isEmpty()){
            U node = toVisit.poll();
            ImmutableList<U> path = paths.get(node).newWith(node);
            if (node.equals(to)){
                return path.toList();
            }
            getNeighbors(node).forEach(i->paths.putIfAbsent(i, path));
        }
        return Lists.mutable.empty();
    }
}
