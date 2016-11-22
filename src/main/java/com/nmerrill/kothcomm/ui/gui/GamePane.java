package com.nmerrill.kothcomm.ui.gui;

import com.nmerrill.kothcomm.game.games.AbstractGame;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class GamePane extends BorderPane {
    private final GameRunnerPane runner;
    public GamePane(AbstractGame game){
        this.runner = new GameRunnerPane(game);
        this.setTop(runner);
        this.getChildren().addListener(new ListChangeListener<Node>() {
            @Override
            public void onChanged(Change<? extends Node> c) {
                c.next();
                c.getAddedSubList().forEach(i -> addNode(i));
                c.getRemoved().forEach(i -> removeNode(i));
                requestLayout();
            }
        });
    }

    private void removeNode(Node node){
        if (node instanceof GameNode) {
            GameNode gameNode = (GameNode) node;
            this.runner.removeGameNode(gameNode);
        }
    }

    private void addNode(Node node){
        if (node instanceof GameNode) {
            GameNode gameNode = (GameNode) node;
            this.runner.addGameNode(gameNode);
        }
    }

}
