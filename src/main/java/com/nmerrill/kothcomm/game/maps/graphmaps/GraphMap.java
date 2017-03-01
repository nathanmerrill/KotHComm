package com.nmerrill.kothcomm.game.maps.graphmaps;

import com.nmerrill.kothcomm.game.maps.GameMap;
import com.nmerrill.kothcomm.game.maps.MapPoint;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.Region;
import com.nmerrill.kothcomm.utils.iterables.Itertools;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.UnsortedSetIterable;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;
import org.eclipse.collections.impl.factory.Sets;

import java.util.*;

public interface GraphMap<U extends MapPoint, T> extends GameMap<U, T>, Iterable<U> {

    UnsortedSetIterable<U> getNeighbors(U origin);

    static <U> MutableSet<U> getNeighbors(U origin, int maxDistance, Function<U, ? extends Iterable<U>> neighborFunction){
        MutableSet<U> points = Sets.mutable.empty();
        points.add(origin);
        MutableSet<U> borders = points.clone();
        for (int i = 1; i <= maxDistance; i++){
            borders = borders.flatCollect(neighborFunction);
            borders.removeIf(points::contains);
            if (borders.isEmpty()){
                break;
            }
            points.addAll(borders);
        }
        return points;
    }

    default UnsortedSetIterable<U> getNeighbors(U origin, int maxDistance){
        return getNeighbors(origin, maxDistance, this::getNeighbors);
    }

    boolean isNeighbor(U origin, U neighbor);

    boolean isEmpty(U point);

    default boolean isFilled(U point) {
        return !isEmpty(point);
    }

    MutableSet<U> locations();

    default U randomLocation(Random random){
        return Itertools.sample(locations().toList());
    }

    default U randomEmptyLocation(Random random){
        return Itertools.sample(emptyLocations().toList());
    }

    default U randomFilledLocation(Random random){
        return Itertools.sample(filledLocations().toList());
    }

    default U randomLocation(){
        return randomLocation(new Random());
    }

    default U randomEmptyLocation(){
        return randomEmptyLocation(new Random());
    }

    default U randomFilledLocation(){
        return randomFilledLocation(new Random());
    }

    default GraphMap<U, T> subMap(Region<U> region){
        return new GraphMapView<>(this, region);
    }

    MutableList<T> items();

    @Override
    default Iterator<U> iterator(){
        return locations().iterator();
    }

    default MutableSet<U> emptyLocations(){
        return locations().select(this::isEmpty);
    }

    default MutableSet<U> filledLocations(){
        return locations().select(this::isFilled);
    }

    default ReadonlyGraphMap<U, T> readonly(){
        return new ReadonlyGraphMap<>(this);
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
