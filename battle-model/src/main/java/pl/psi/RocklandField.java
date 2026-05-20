package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.MagicLevel;

public class RocklandField extends SpecialField {
    public RocklandField() {
        super(Color.BROWN, FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (SpecialField.canFly(aCreature.getName())) {
            return;
        }
        aCreature.setEarthMagicLevel(MagicLevel.EXPERT);
    }
}
