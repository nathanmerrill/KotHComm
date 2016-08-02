package KoTHComm.game.maps.generators.mazes;

import KoTHComm.game.maps.generators.Generator;
import KoTHComm.game.maps.gridmaps.GridMap;
import KoTHComm.game.maps.gridmaps.Point2D;
import KoTHComm.utils.Tools;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ellers implements Generator<GridMap<Point2D, ?>> {
    private final Random random;
    private final int minX, maxX, minY, maxY;
    public Ellers(Point2D p1, Point2D p2, Random random){
        this.random = random;
        if (p1.getX() < p2.getX()){
            minX = p1.getX();
            maxX = p2.getX()+1;
        } else {
            minX = p2.getX();
            maxX = p1.getX()+1;
        }
        if (p1.getY() < p2.getY()){
            minY = p1.getY();
            maxY = p2.getY()+1;
        } else {
            minY = p2.getY();
            maxY = p1.getY()+1;
        }
    }
    public Ellers(Point2D p1, Point2D p2){
        this(p1, p2, new Random());
    }

    @Override
    public void generate(GridMap<Point2D, ?> map) {
        List<Integer> states = Tools.range(maxX-minX);
        IntStream.range(minY, maxY).forEach(y -> {
            IntStream.range(minX, maxX).forEach(x->map.put(new Point2D(x, y), null));
            for (int x: connectNeighbors(states)) {
                map.connect(new Point2D(minX+x, y), new Point2D(minX+x+1, y));
            }
        });

    }

    private List<Integer> nextLine(List<Integer> states){
        return null;
    }


    private List<Integer> connectNeighbors(List<Integer> states){
        return IntStream.range(0, states.size()-1)
                .filter(x -> !states.get(x+1).equals(states.get(x)) && random.nextBoolean())
                .mapToObj(i -> i)
                .collect(Collectors.toList());

    }
    /*
    states = range(self.maze.width)
        for y in xrange(self.maze.height):
    states = self.generate_line(y, states)

    def generate_line(self, y, states):
            self.connect_neighbors(y, states)
            if y == self.maze.height-1:
            while not all(x == states[0] for x in states):x
            self.connect_neighbors(y, states)
            else:
    states = self.connect_lines(y, states)
            return states

    def connect_neighbors(self, y, states):
            for x in xrange(self.maze.width-1):
            if states[x] != states[x+1]:
            if random.random() < self.weight:
            self.maze.get((x, y)).connect_to_cell(Directions(Directions.RIGHT))
    to_replace = states[x+1]
            for index, value in enumerate(states):
            if value == to_replace:
    states[index] = states[x]

    def connect_lines(self, y, states):
    new_states = range(max(states)+1, max(states)+1+len(states))
    states_left = {state for state in states}
    states_to_pick = range(len(states))
            random.shuffle(states_to_pick)
            for index in states_to_pick:
            if states[index] in states_left or random.random() >= self.weight:
            self.maze.get((index, y)).connect_to_cell(Directions(Directions.BOTTOM))
    new_states[index] = states[index]
            if states[index] in states_left:
            states_left.remove(states[index])
            return new_states
            */
}
