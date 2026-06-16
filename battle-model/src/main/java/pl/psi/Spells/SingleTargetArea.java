package pl.psi.Spells;

import pl.psi.BattlePoint;

import java.util.List;

public class SingleTargetArea implements SpellAreaIf {
    @Override
    public List<BattlePoint> getArea(BattlePoint centerPoint) {
        return List.of(centerPoint);
    }
}
