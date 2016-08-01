package game.maps.gridmaps.bounds;

import game.maps.gridmaps.Point2D;

public class TriangularBounds implements Bounds<Point2D>{
    private final double a;
    private final int s1, s2, s3, t1, t2, t3;

    public TriangularBounds(Point2D p0, Point2D p1, Point2D p2){
        double area = .5*(
                - p1.getY() * p2.getX()
                + p0.getY() * (p2.getX() - p1.getX())
                + p0.getX() * (p1.getY() - p2.getY())
                + p1.getX() * p2.getY());
        a = 1/(2*area);
        s1 = p0.getY()*p2.getX() - p0.getX()*p2.getY();
        s2 = p2.getY() - p0.getY();
        s3 = p0.getX() - p2.getX();
        t1 = p0.getX()*p1.getY() - p0.getY()*p1.getX();
        t2 = p0.getY() - p1.getY();
        t3 = p1.getX() - p0.getX();
    }

    public TriangularBounds(int size){
        this(new Point2D(0,0), new Point2D(0, size), new Point2D(size, 0));
    }

    @Override
    public Point2D wrap(Point2D point) {
        return point;
    }

    @Override
    public boolean outOfBounds(Point2D p) {
        double s = a * (s1 + s2*p.getX() + s3*p.getY());
        if (s < 0){
            return true;
        }
        double t = a * (t1 + t2*p.getX() + t3*p.getY());
        return t < 0 || 1-s-t < 0;
    }
}
