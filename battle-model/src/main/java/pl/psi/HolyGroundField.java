package pl.psi;
import pl.psi.creatures.Creature;
import pl.psi.creatures.Faction;

public class HolyGroundField extends SpecialField {
    public HolyGroundField() {
        super(Color.YELLOW, FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (SpecialField.canFly(aCreature.getName())) {
            return;
        }
        if (aCreature.getFaction() == Faction.GOOD) {
            aCreature.setMorale(aCreature.getMorale() + 1);
        } else if (aCreature.getFaction() == Faction.EVIL) {
            aCreature.setMorale(aCreature.getMorale() - 1);
        }
    }
}
