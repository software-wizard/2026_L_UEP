package pl.psi;

import pl.psi.creatures.Creature;
import pl.psi.creatures.MagicLevel;

public class MagicPlainsField extends SpecialField {
    public MagicPlainsField() {
        super(FieldName.MAGIC_PLAINS);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        aCreature.setEarthMagicLevel(MagicLevel.EXPERT);
        aCreature.setAirMagicLevel(MagicLevel.EXPERT);
        aCreature.setWaterMagicLevel(MagicLevel.EXPERT);
        aCreature.setFireMagicLevel(MagicLevel.EXPERT);
    }
}
