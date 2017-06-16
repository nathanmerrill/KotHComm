package com.nmerrill.kothcomm.ui.text;

import com.nmerrill.kothcomm.game.scoring.Scoreboard;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;

import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;

public class TextUI {
    public final PrintStream out;
    private boolean showScores;
    private Instant start;
    public TextUI(PrintStream out){
        this.out = out;
        showScores = true;
    }

    public TextUI(){
        this(System.out);
    }

    public void showScores(){
        showScores = true;
    }

    public void hideScores(){
        showScores = false;
    }

    public <T> void printScoreboard(Scoreboard<T> scoreboard, TableBuilder builder) {
        out.println();
        if (scoreboard.isEmpty()) {
            out.println("No scores");
        }
        MutableList<MutableList<String>> table = Lists.mutable.empty();
        if (builder.hasHeader()) {
            MutableList<String> header = Lists.mutable.of("Rank", "Name");
            if (showScores) {
                header.add("Score");
            }
            table.add(header);
        }
        int rank = 1;
        for (MutableSet<T> scores :scoreboard.rank()){
            for (T item: scores){
                MutableList<String> row = Lists.mutable.of(rank+"", item.toString());
                if (showScores){
                    row.add(scoreboard.getScore(item)+"");
                }
                table.add(row);
            }
            rank += scores.size();
        }
        out.println(builder.display(table));
    }

    public void printScoreboard(Scoreboard<?> scoreboard){
        TableBuilder builder = new TableBuilder();
        builder.rightAlign(0);
        builder.rightAlign(2);
        builder.hasHeader(true);
        builder.setBorderType(TableBuilder.BorderType.ASCII);
        printScoreboard(scoreboard, builder);
    }

    private Duration timeRemaining(int current, int max){
        if (start == null){
            start = Instant.now();
            return Duration.ofMillis(0);
        }
        Instant currentTime = Instant.now();
        Duration duration = Duration.between(start, currentTime);
        return duration.dividedBy(current).multipliedBy(max-current);
    }

    public void printProgress(int current, int max){
        int percent = current * 100/max;
        Duration remaining = timeRemaining(current, max);
        long hours = remaining.toHours();
        remaining = remaining.minus(hours, HOURS);
        long minutes = remaining.toMinutes();
        remaining = remaining.minus(minutes, MINUTES);
        long seconds = remaining.getSeconds();
        out.print(
                "\r" +
                String.format(" %d%% [", percent) +
                repeated('=', percent) +
                '>' +
                repeated(' ', 100 - percent) +
                "] " +
                current + '/' + max +
                " " + hours + "h " + minutes + "m " + seconds + "s"
        );
    }

    private String repeated(char character, int count){
        char[] column = new char[count];
        Arrays.fill(column, character);
        return new String(column);
    }


}
