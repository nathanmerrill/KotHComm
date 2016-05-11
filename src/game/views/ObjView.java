package game.views;

import javax.swing.*;

public abstract class ObjView<T> extends JFrame {
    protected final T object;
    public ObjView(T object){
        this.object = object;
        setTitle(this.object.toString());
    }

}
