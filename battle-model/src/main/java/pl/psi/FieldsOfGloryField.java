package pl.psi;

import pl.psi.creatures.Creature;

public class FieldsOfGloryField extends SpecialField {
    public FieldsOfGloryField() {
        super(Color.GRAY, FieldName.FIELDS_OF_GLORY);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        aCreature.setLuck(aCreature.getLuck() - 2);
    }
}
