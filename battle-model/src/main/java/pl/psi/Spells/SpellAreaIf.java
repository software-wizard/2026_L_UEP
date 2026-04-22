package pl.psi.Spells;

import pl.psi.BattlePoint;

import java.util.List;

public interface SpellAreaIf {
    List<BattlePoint> getArea(BattlePoint centerPoint);
}
