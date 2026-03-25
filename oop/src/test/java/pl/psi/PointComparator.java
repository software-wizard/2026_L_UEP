package pl.psi;

public class PointComparator {
    static boolean compare(Point3D aP1, Point3D aP2) {

        if(aP1 instanceof Point3DInherit){
            return false;
        }
        else {
            return aP1.getX() == aP2.getX() &&
                    aP1.getY() == aP2.getY() &&
                    aP1.getZ() == aP2.getZ();
        }
    }
}
