package pl.psi;

import pl.psi.creatures.Creature;

public class CrackedIceField extends SpecialField {
    public CrackedIceField() {
        super(FieldName.CRACKED_ICE);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        aCreature.setArmorModifier(aCreature.getArmorModifier() - 5);
    }
}
