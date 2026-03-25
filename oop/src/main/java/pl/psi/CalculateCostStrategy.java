package pl.psi;

public class CalculateCostStrategy {
    int getCost(Point3DComposition aPoint3DComposition) {
        if(aPoint3DComposition.getX() < 0){
            return 0;
        }
        return (aPoint3DComposition.getX() + aPoint3DComposition.getY()) * aPoint3DComposition.getZ();
    }
}
