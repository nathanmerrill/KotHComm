package com.nmerrill.kothcomm.ui.gui;

import com.nmerrill.kothcomm.game.maps.Point2D;
import com.nmerrill.kothcomm.game.maps.graphmaps.GraphMap;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;

import java.util.function.Consumer;

public class GraphMap2DView<U> extends Pane implements GameNode{

    private final Representation<U> representation;
    private final MutableList<Consumer<U>> listeners;
    private final GraphMap<Point2D, U> map;
    private final Canvas canvas;
    private int minX, minY, maxX, maxY;

    public GraphMap2DView(GraphMap<Point2D, U> map, Representation<U> representation){
        this.canvas = new Canvas(){
            @Override
            public boolean isResizable() {
                return true;
            }
        };
        this.representation = representation;
        this.listeners = Lists.mutable.empty();
        this.map = map;
        canvas.widthProperty().addListener(evt -> draw());
        canvas.heightProperty().addListener(evt -> draw());
        canvas.widthProperty().bind(this.widthProperty());
        canvas.heightProperty().bind(this.heightProperty());
        this.getChildren().add(canvas);
    }

    public GraphMap2DView(GraphMap<Point2D, U> map){
        this(map, new RandomRepresentation<>());
    }

    public void addListener(Consumer<U> onClick){
        listeners.add(onClick);
    }

    @Override
    public void draw(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0,0,canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        MutableMap<Point2D, U> map = this.map.toMap();
        RichIterable<Point2D> points = map.keysView();
        minX = Math.min(points.minBy(Point2D::getX).getX(), minX);
        maxX = Math.max(points.maxBy(Point2D::getX).getX()+1, maxX);
        minY = Math.min(points.minBy(Point2D::getY).getY()-1, minY);
        maxY = Math.max(points.maxBy(Point2D::getY).getY(), maxY);

        double charHeight = canvas.getHeight()/(maxY-minY);
        double charWidth = canvas.getWidth()/(maxX-minX);
        gc.setFont(new Font("Monospaced", charHeight));

        points.toMap(i -> i, i -> representation.represent(map.get(i)))
                .forEach((point, representation) -> {
                    double x = (point.getX()-minX)*charWidth;
                    double y = (point.getY()-minY)*charHeight;
                    gc.setFill(representation.getTwo());
                    gc.fillText(representation.getOne().toString(),x,y);
                });


        this.setOnMouseClicked(event -> {
            int x = (int) Math.round((event.getX()/charWidth)+minX);
            int y = (int) Math.round((event.getY()/charHeight)+minY);
            U item = map.get(new Point2D(x, y));
            listeners.forEachWith(Consumer::accept, item);
        });
    }
}
