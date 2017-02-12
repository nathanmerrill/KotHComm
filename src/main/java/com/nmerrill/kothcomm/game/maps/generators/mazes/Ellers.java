package com.nmerrill.kothcomm.game.maps.generators.mazes;

import com.nmerrill.kothcomm.game.maps.Point2D;
import com.nmerrill.kothcomm.game.maps.generators.Generator;
import com.nmerrill.kothcomm.game.maps.graphmaps.GraphGraphMap;
import com.nmerrill.kothcomm.game.maps.graphmaps.bounds.point2D.SquareRegion;
import com.nmerrill.kothcomm.utils.iterables.Itertools;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableBooleanList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.set.primitive.MutableIntSet;
import org.eclipse.collections.impl.factory.primitive.BooleanLists;
import org.eclipse.collections.impl.factory.primitive.IntSets;

import java.util.Random;

public class Ellers implements Generator<GraphGraphMap<Point2D, ?>> {
    private final Random random;
    private final SquareRegion region;
    private MutableList<MutableIntSet> currentLine;
    private Point2D currentOffset;
    private GraphGraphMap<Point2D, ?> currentMap;

    public Ellers(SquareRegion region, Random random) {
        this.random = random;
        this.region = region;
    }

    public Ellers(SquareRegion region) {
        this(region, new Random());
    }

    @Override
    public void generate(GraphGraphMap<Point2D, ?> map) {
        int width = region.getWidth();
        int height = region.getHeight();
        currentLine = Itertools.range(0, width).collect(IntSets.mutable::of).toList();
        currentMap = map;
        for (int y = 0; y < height; y++) {
            currentOffset = new Point2D(region.getLeft(), region.getBottom()+y);
            connectSets();
            if (y == height -1){
                break;
            }
            newLine();
        }
        while (currentLine.toSet().size() > 1){
            connectSets();
        }
    }

    private void connectSets(){
        for (int x = 0; x < region.getWidth() - 1; x++) {
            MutableIntSet currentSet = currentLine.get(x);
            MutableIntSet adjacentSet = currentLine.get(x + 1);
            if (currentSet.equals(adjacentSet)) {
                break;
            }
            if (random.nextBoolean()) {
                break;
            }
            Point2D currentPoint = currentOffset.moveX(x);
            currentMap.connect(currentPoint, currentPoint.moveX(1));
            MutableIntSet smaller, larger;
            if (currentSet.size() < adjacentSet.size()) {
                smaller = currentSet;
                larger = adjacentSet;
            } else {
                smaller = adjacentSet;
                larger = currentSet;
            }
            larger.addAll(smaller);
            smaller.forEach(i -> currentLine.set(i, adjacentSet));
        }
    }

    private void newLine(){
        currentLine.toSet().forEach(set -> {
            MutableIntList list = set.toList();
            MutableBooleanList bits = randomBits(set.size());
            for (int i = 0; i < set.size(); i++) {
                int x = list.get(i);
                if (bits.get(i)) {
                    Point2D currentPoint = currentOffset.move(x, 0);
                    currentMap.connect(currentPoint, currentPoint.moveY(1));
                } else {
                    set.remove(list.get(i));
                    currentLine.set(x, IntSets.mutable.of(x));
                }
            }
        });
    }

    private MutableBooleanList randomBits(int count) {
        MutableBooleanList bits = BooleanLists.mutable.empty();
        if (count < 32) {
            int intBits = random.nextInt((1 << count) - 1);
            for (int i = 0; i < count; i++) {
                bits.add(((intBits >> i) & 1) == 0);
            }
            return bits;
        }
        while (true) {
            bits.clear();
            for (int i = 0; i < count; i++) {
                bits.add(random.nextBoolean());
            }
            if (bits.anySatisfy(i -> i)) {
                return bits;
            }
        }
    }

}
