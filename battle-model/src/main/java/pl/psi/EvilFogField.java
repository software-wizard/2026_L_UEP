package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.Faction;

public class EvilFogField extends SpecialField {
    public EvilFogField() {
        super(Color.GRAY, FieldName.DEBUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (SpecialField.canFly(aCreature.getName())) {
            return;
        }
        if (aCreature.getFaction() == Faction.EVIL) {
            aCreature.setMorale(aCreature.getMorale() + 1);
        } else if (aCreature.getFaction() == Faction.GOOD) {
            aCreature.setMorale(aCreature.getMorale() - 1);
        }
    }
}
