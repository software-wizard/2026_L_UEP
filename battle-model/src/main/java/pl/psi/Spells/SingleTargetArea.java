package pl.psi.Spells;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.psi.BattlePoint;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true, value = {"empty"})
public class SingleTargetArea implements SpellAreaIf {
    public SingleTargetArea() {}
    @Override
    public List<BattlePoint> getArea(BattlePoint centerPoint) {
        return List.of(centerPoint);
    }

}
