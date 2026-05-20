package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.MagicLevel;

public class MagicCloudsField extends SpecialField {
    public MagicCloudsField() {
        super(Color.GRAY, FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (SpecialField.canFly(aCreature.getName())) {
            return;
        }
        aCreature.setAirMagicLevel(MagicLevel.EXPERT);
    }
}
