package com.ppcg.kothcomm.game.maps.generators.mazes;

import com.ppcg.kothcomm.game.maps.MapPoint;
import com.ppcg.kothcomm.game.maps.generators.Generator;
import com.ppcg.kothcomm.game.maps.gridmaps.GridMap;
import com.ppcg.kothcomm.utils.Tools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Twin;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.tuple.Tuples;

import java.util.Random;
import java.util.function.ToIntFunction;

public class GrowingTree<U extends MapPoint> implements Generator<GridMap<U, ?>> {

    @SuppressWarnings({"SameReturnValue", "UnusedParameters"})
    public static int selectOldest(MutableList<? extends MapPoint> points){
        return 0;
    }
    public static int selectNewest(MutableList<? extends MapPoint> points){
        return points.size()-1;
    }
    public static <U extends MapPoint> ToIntFunction<MutableList<U>> randomSelection(){
        return randomSelection(new Random());
    }
    public static <U extends MapPoint> ToIntFunction<MutableList<U>> randomSelection(Random random){
        return (MutableList<U> list) -> random.nextInt(list.size());
    }

    private final ToIntFunction<MutableList<U>> selectionMethod;
    private final Random random;
    public GrowingTree(ToIntFunction<MutableList<U>> selectionMethod, Random random){
        this.selectionMethod = selectionMethod;
        this.random = random;
    }
    public GrowingTree(ToIntFunction<MutableList<U>> selectionMethod){
        this(selectionMethod, new Random());
    }
    public GrowingTree(Random random){
        this(randomSelection(random), random);
    }

    public GrowingTree(){
        this(new Random());
    }

    @Override
    public void generate(GridMap<U, ?> map) {
        MutableList<U> locations = map.locations().toList();
        U start = locations.get(random.nextInt(locations.size()));
        MutableList<U> borders = map.getNeighbors(start).toList();
        MutableList<U> visited = Lists.mutable.with();
        MutableSet<Twin<U>> connections = Sets.mutable.empty();
        visited.add(start);
        
        while (!borders.isEmpty()){
            U point = borders.remove(selectionMethod.applyAsInt(borders));
            MutableSet<U> neighbors = map.getNeighbors(point);
            neighbors.removeIf(visited::contains);
            U neighbor = Tools.sample(neighbors.toList(), random);
            Twin<U> connection = Tuples.twin(point, neighbor);
            connections.add(connection);
            connections.add(connection.swap());
            borders.addAll(neighbors);
        }
        for (U point: map.locations()){
            MutableSet<U> neighbors = map.getNeighbors(point);
            neighbors.removeIf(n -> connections.contains(Tuples.twin(point, n)));
            neighbors.forEach(n -> map.disconnect(point, n));
        }
    }

}
