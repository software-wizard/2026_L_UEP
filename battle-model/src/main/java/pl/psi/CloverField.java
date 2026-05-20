package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.Faction;

public class CloverField extends SpecialField {
    public CloverField() {
        super(Color.GREEN, FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (shouldIgnore(aCreature)) {
            return;
        }
        if (aCreature.getFaction() == Faction.NEUTRAL) {
            aCreature.setLuck(aCreature.getLuck() + 2);
        }
    }
}
