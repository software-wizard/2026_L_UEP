package pl.psi;

import pl.psi.creatures.Creature;

public class DmgField extends SpecialField {

    public DmgField() {
        super(FieldName.DMG_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        aCreature.applyDamage(aCreature, 20);
    }
}
