package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.MagicLevel;

public class LucidPoolsField extends SpecialField {
    public LucidPoolsField() {
        super(FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        aCreature.setWaterMagicLevel(MagicLevel.EXPERT);
    }
}
