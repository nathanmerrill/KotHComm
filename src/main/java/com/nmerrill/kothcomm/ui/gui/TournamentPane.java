package com.nmerrill.kothcomm.ui.gui;

import com.nmerrill.kothcomm.game.AbstractPlayer;
import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.runners.TournamentRunner;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.HashMap;
import java.util.function.Function;

public class TournamentPane<T extends AbstractPlayer<T>, U extends AbstractGame<T>> extends BorderPane {
    private final TournamentRunner<T> tournamentRunner;
    private final MenuBar menubar;
    private final TabPane games;
    private final Function<U, ? extends Pane> gamePane;
    private final HashMap<U, Tab> tabs;
    public TournamentPane(TournamentRunner<T> tournamentRunner, Function<U, ? extends Pane> gamePane){
        this.tournamentRunner = tournamentRunner;
        menubar = new MenuBar();
        this.setTop(menubar);
        createTournamentMenu();
        games = new TabPane();
        this.setCenter(games);
        this.gamePane = gamePane;
        this.tabs = new HashMap<>();
        addGame();
    }

    public boolean removeTab(U game){
        return games.getTabs().remove(tabs.get(game));
    }

    private void createTournamentMenu(){
        Menu menu = new Menu("Tournament");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> addGame());
        MenuItem showScores = new MenuItem("Overall scores");
        showScores.setOnAction(event -> showScores());
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> System.exit(0));
        menu.getItems().addAll(newGame, showScores, exit);
        addMenu(menu);
    }

    public void addMenu(Menu menu){
        menubar.getMenus().add(menu);
    }

    public boolean removeMenu(Menu menu){
        return menubar.getMenus().remove(menu);
    }

    @SuppressWarnings("unchecked")
    public void addGame(){
        U game = (U) tournamentRunner.createGame();
        game.start();
        Tab tab = new Tab("Game "+games.getTabs().size(),gamePane.apply(game));
        games.getTabs().add(tab);
        tabs.put(game, tab);
    }

    public void showScores(){
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Scores");
        stage.setScene(new Scene(new ScoreboardView<>(tournamentRunner.scoreboard())));
        stage.show();
    }
}
