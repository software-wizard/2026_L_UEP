package pl.psi;

import lombok.Getter;

public class Point3DComposition implements Point3D, Point3DPrint{
    private Point2D point;
    @Getter
    private int z;

    private CalculateCostStrategy obj = new CalculateCostStrategy();

    public Point3DComposition(int x, int y, int z) {
        point = new Point2D(x,y);
        this.z = z;
    }

    @Override
    public int getX() {
        return point.getX();
    }

    @Override
    public int getY() {
        return point.getY();
    }

    public void xyz(){
        System.out.print("Point3DComposition");
    }

    public int getCost(){
        return obj.getCost(this);
    }
}
