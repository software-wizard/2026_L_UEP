package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.MagicLevel;

public class LucidPoolsField extends SpecialField {
    public LucidPoolsField() {
        super(Color.CYAN, FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (SpecialField.canFly(aCreature.getName())) {
            return;
        }
        aCreature.setWaterMagicLevel(MagicLevel.EXPERT);
    }
}
