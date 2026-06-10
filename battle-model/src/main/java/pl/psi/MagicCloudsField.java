package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.MagicLevel;

public class MagicCloudsField extends SpecialField {
    public MagicCloudsField() {
        super(FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        aCreature.setAirMagicLevel(MagicLevel.EXPERT);
    }
}
