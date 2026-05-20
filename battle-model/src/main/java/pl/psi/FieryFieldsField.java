package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.MagicLevel;

public class FieryFieldsField extends SpecialField {
    public FieryFieldsField() {
        super(Color.RED, FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (SpecialField.canFly(aCreature.getName())) {
            return;
        }
        aCreature.setFireMagicLevel(MagicLevel.EXPERT);
    }
}
