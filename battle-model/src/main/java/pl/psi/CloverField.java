package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.Faction;

public class CloverField extends SpecialField {
    public CloverField() {
        super(Color.ORANGE, FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (SpecialField.canFly(aCreature.getName())) {
            return;
        }
        if (aCreature.getFaction() == Faction.NEUTRAL) {
            aCreature.setLuck(aCreature.getLuck() + 2);
        }
    }
}
