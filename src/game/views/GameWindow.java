package game.views;

import game.Game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameWindow {
    private final List<MapView> maps;
    private final JFrame frame;
    private final JMenuBar menu;
    private final Game game;
    private boolean running;
    private GameRunner runner;
    public GameWindow(Game game){
        this.game = game;
        maps = new ArrayList<>();
        frame = new JFrame();
        menu = new JMenuBar();
        runner = new GameRunner();
        JButton start = new JButton("Start");
        JButton step = new JButton("Step");
        JButton stop = new JButton("Stop");
        JButton exit = new JButton("Exit");
        start.addActionListener((a) -> {
            if (runner.isCancelled())
                GameWindow.this.runner = new GameRunner();
            runner.execute();
        });
        stop.addActionListener((a) -> runner.cancel(true));
        step.addActionListener((a) -> {
            game.next();
            repaint();
        });
        exit.addActionListener((a) -> System.exit(0));
        menu.add(start);
        menu.add(stop);
        menu.add(step);
        menu.add(exit);
        frame.setJMenuBar(menu);

    }
    class GameRunner extends SwingWorker<Integer, Integer> {
        @Override
        protected Integer doInBackground() throws Exception {
            while (game.next()){
                this.publish(0);
                Thread.sleep(0);
            }
            return 0;
        }

        @Override
        protected void process(List<Integer> chunks) {
            super.process(chunks);
            repaint();
        }
    }
    private void repaint(){
        maps.forEach(MapView::repaint);
    }

    public void addMap(MapView view){
        maps.add(view);
    }

}
