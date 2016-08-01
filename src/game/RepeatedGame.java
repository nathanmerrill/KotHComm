package game;

public abstract class RepeatedGame<T extends GamePlayer> extends AbstractGame<T> {
    private int iterations;
    public RepeatedGame(int iterations){
        this.iterations = iterations;
    }
    @Override
    protected boolean step() {
        iterations--;
        return iterations <= 0;
    }
    protected abstract void nextStep();
}
