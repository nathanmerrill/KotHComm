package com.nmerrill.kothcomm.ui.gui;

import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.eclipse.collections.api.tuple.primitive.ObjectDoublePair;

public class ScoreboardView<T> extends TableView<ObjectDoublePair<T>> implements GameNode {

    private final Scoreboard<T> scoreboard;
    public ScoreboardView(Scoreboard<T> scoreboard){
        this.setEditable(false);
        this.scoreboard = scoreboard;
        TableColumn<ObjectDoublePair<T>, T> name = new TableColumn<>("Name");
        name.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getOne()));
        TableColumn<ObjectDoublePair<T>, Double> score = new TableColumn<>("Score");
        score.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getTwo()));
        this.getColumns().add(name);
        this.getColumns().add(score);
        draw();
    }

    @Override
    public void draw() {
        this.getItems().setAll(scoreboard.scoresOrdered());
    }

}
