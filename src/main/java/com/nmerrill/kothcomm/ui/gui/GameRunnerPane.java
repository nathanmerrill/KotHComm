package com.nmerrill.kothcomm.ui.gui;

import com.nmerrill.kothcomm.game.games.AbstractGame;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

public class GameRunnerPane extends HBox {
    private Button startButton, stopButton, stepButton;
    private final MutableSet<GameNode> toUpdate;
    private final AbstractGame game;

    public GameRunnerPane(AbstractGame game) {
        Service<Void> service = service();
        this.game = game;
        stepButton = button("Step", event -> {
            service.cancel();
            step();
        });
        startButton = button("Start", event -> {
            service.restart();
            startButton.setDisable(true);
            stopButton.setDisable(false);
        });
        stopButton = button("Stop", event -> {
            service.cancel();
            startButton.setDisable(false);
            stopButton.setDisable(true);
        });
        stopButton.setDisable(true);
        this.getChildren().addAll(startButton, stopButton, stepButton);
        toUpdate = Sets.mutable.empty();

    }

    public void addGameNode(GameNode node){
        toUpdate.add(node);
    }

    public boolean removeGameNode(GameNode node){
        return toUpdate.remove(node);
    }

    private Service<Void> service(){
        return new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (step()) {
                            if (isCancelled()) {
                                break;
                            }
                        }
                        return null;
                    }
                };
            }
        };
    }

    private boolean step() {
        boolean ret = game.next();
        toUpdate.forEach(GameNode::draw);
        return ret;
    }

    private Button button(String text, EventHandler<MouseEvent> event) {
        Button button = new Button(text);
        button.setOnMouseClicked(event);
        return button;
    }
}
