package pl.psi;
import pl.psi.creatures.Creature;

public class FavorableWindsField extends SpecialField {
    public FavorableWindsField() {
        super(Color.CYAN, FieldName.BUFF_FIELD);
    }

    @Override
    public void doSomething(Creature aCreature) {
        if (SpecialField.canFly(aCreature.getName())) {
            return;
        }
        aCreature.setMovementCostModifier(aCreature.getMovementCostModifier() - 0.34);
    }
}
