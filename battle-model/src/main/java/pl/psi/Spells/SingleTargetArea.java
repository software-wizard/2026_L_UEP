package pl.psi.Spells;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.psi.BattlePoint;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // <-- TA JEDNA LINIJKA NAPRAWIA CAŁY PROBLEM
public class SingleTargetArea implements SpellAreaIf {
    @Override
    public List<BattlePoint> getArea(BattlePoint centerPoint) {
        return List.of(centerPoint);
    }
}
