package pl.psi.Spells;

import pl.psi.BattlePoint;

import java.util.ArrayList;
import java.util.List;

public class RadiusArea implements SpellAreaIf {

    private double radius;

    public RadiusArea(double radius) {
        this.radius = radius;
    }

    public RadiusArea() {}

    @Override
    public List<BattlePoint> getArea(BattlePoint centerPoint) {
        List<BattlePoint> area = new ArrayList<>();
        // In a typical 14x14 grid, we iterate over a bounding box
        int intRadius = (int) Math.ceil(radius);
        
        for (int x = centerPoint.getX() - intRadius; x <= centerPoint.getX() + intRadius; x++) {
            for (int y = centerPoint.getY() - intRadius; y <= centerPoint.getY() + intRadius; y++) {
                BattlePoint p = new BattlePoint(x, y);
                if (centerPoint.distance(p) <= radius) {
                    area.add(p);
                }
            }
        }
        return area;
    }
}
