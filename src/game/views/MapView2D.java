package game.views;


import game.maps.GameMap;
import game.maps.gridmaps.GridMap;
import game.maps.gridmaps.Point2D;

import javax.swing.*;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


public class MapView<T> extends JPanel{
    private final GridMap<Point2D, T> map;
    private final List<ObjView<T>> objects;
    private int xScale;
    private int yScale;
    public MapView(GridMap<Point2D, T> map, Function<T, ObjView<T>> objView){
        this.map = map;
        this.xScale = 10;
        this.yScale = 10;
        this.objects = new ArrayList<>();
        this.setFont(new Font("Courier", Font.PLAIN, 6));
        if (objView != null) {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    GridPoint point = new GridPoint(e.getX() / xScale, e.getY() / yScale);
                    if (map.contains(point)) {
                        ObjView<T> view = objView.apply(map.get(point));
                        view.setVisible(true);
                        objects.add(view);
                    }
                }
            });
        }
    }



    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Map.Entry<GridPoint, T> entry : map.toMap().entrySet()){
            g.drawString(entry.getValue().toString(), entry.getKey().getX(), entry.getKey().getY());
        }
    }




}
