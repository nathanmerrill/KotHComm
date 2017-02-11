package com.nmerrill.kothcomm.game;

import com.nmerrill.kothcomm.communication.Arguments;
import com.nmerrill.kothcomm.communication.Downloader;
import com.nmerrill.kothcomm.communication.LanguageLoader;
import com.nmerrill.kothcomm.communication.languages.Language;
import com.nmerrill.kothcomm.communication.languages.local.LocalJavaLoader;
import com.nmerrill.kothcomm.game.games.AbstractGame;
import com.nmerrill.kothcomm.game.players.AbstractPlayer;
import com.nmerrill.kothcomm.game.players.Submission;
import com.nmerrill.kothcomm.game.scoring.ItemAggregator;
import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import com.nmerrill.kothcomm.game.tournaments.Sampling;
import com.nmerrill.kothcomm.game.tournaments.Tournament;
import com.nmerrill.kothcomm.ui.text.TextUI;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class KotHComm<T extends AbstractPlayer<T>, U extends AbstractGame<T>> {
    private final Supplier<U> gameSupplier;
    private final MutableList<Language<T>> languages;
    private final LocalJavaLoader<T> localLoader;
    private BiFunction<MutableList<Submission<T>>, Random, Tournament<Submission<T>>> tournamentSupplier;
    private Arguments arguments;
    private TextUI printer;
    private int gameSize;
    private boolean shouldDownload;

    public KotHComm(Supplier<U> gameSupplier){
        this.gameSupplier = gameSupplier;
        this.languages = Lists.mutable.empty();
        localLoader = new LocalJavaLoader<>();
        this.languages.add(localLoader);
        this.tournamentSupplier = Sampling::new;
        this.gameSize = 2;
        this.arguments = new Arguments();
        this.shouldDownload = true;
        this.printer = new TextUI();
    }

    public void addLanguage(Language<T> language){
        this.languages.add(language);
    }

    public void addSubmission(String name, Supplier<T> playerConstructor){
        this.localLoader.register(name, playerConstructor);
    }

    public void setGameSize(int gameSize) {
        this.gameSize = gameSize;
    }

    public void setPrinter(TextUI printer) {
        this.printer = printer;
    }

    public void shouldDownload(boolean shouldDownload) {
        this.shouldDownload = shouldDownload;
    }

    public void setArgumentParser(Arguments arguments) {
        this.arguments = arguments;
    }

    public void setTournament(BiFunction<MutableList<Submission<T>>, Random, Tournament<Submission<T>>> tournamentSupplier) {
        this.tournamentSupplier = tournamentSupplier;
    }

    public void setTournament(Function<MutableList<Submission<T>>, Tournament<Submission<T>>> tournamentSupplier) {
        this.tournamentSupplier = (i, j) -> tournamentSupplier.apply(i);
    }

    public void setTournament(Supplier<Tournament<Submission<T>>> tournamentSupplier){
        this.tournamentSupplier = (i, j) -> tournamentSupplier.get();
    }

    public void run(String[] args){
        Arguments.parse(args);
        LanguageLoader<T> loader = new LanguageLoader<>(arguments);
        languages.forEach(loader::addLoader);
        if (shouldDownload && arguments.validQuestionID()) {
            new Downloader(loader, arguments.questionID).downloadQuestions();
        }
        MutableList<Submission<T>> players = loader.load();
        Random random = arguments.getRandom();
        Tournament<Submission<T>> tournament = tournamentSupplier.apply(players, random);

        TournamentRunner<T, U> runner =
                new TournamentRunner<>(tournament, new ItemAggregator<>(), gameSize, gameSupplier, random);
        printer.out.println("Running "+arguments.iterations+" games");
        for (int i = 0; i < arguments.iterations; i++) {
            printer.printProgress(i, arguments.iterations);
            runner.createGame().run();
        }
        printer.printProgress(arguments.iterations, arguments.iterations);
        Scoreboard<Submission<T>> scoreboard = runner.scoreboard();
        printer.printScoreboard(scoreboard);
    }
}
