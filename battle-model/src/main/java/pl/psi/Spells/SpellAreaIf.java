package pl.psi.Spells;

import pl.psi.BattlePoint;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public interface SpellAreaIf {
    List<BattlePoint> getArea(BattlePoint centerPoint);
}
