package pl.psi;


import lombok.Getter;
import lombok.Setter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;

@Getter
@Setter
public abstract class SpecialField {
    public enum Color{
        CYAN, BROWN, ORANGE, YELLOW, GRAY, RED, GREEN
    }

    public enum FieldName{
        DMG_FIELD, SPELL_FIELD, BUFF_FIELD, FIELD_CAN_ONLY_BE_FLOWN, DEBUFF_FIELD, FIRE_FIELD, CRACKED_ICE, QUICKSAND, FIELDS_OF_GLORY, MAGIC_PLAINS
    }

    @Getter
    private Color color;
    @Getter
    private FieldName fieldName;

    protected SpecialField(Color aColor, FieldName aFieldName) {
        color = aColor;
        fieldName = aFieldName;
    }

    public abstract void doSomething(Creature aCreature);

    public boolean isNegative() {
        return fieldName == FieldName.DMG_FIELD || fieldName == FieldName.DEBUFF_FIELD || fieldName == FieldName.QUICKSAND || fieldName == FieldName.FIELDS_OF_GLORY || fieldName == FieldName.CRACKED_ICE;
    }

    public static boolean canFly(Creature aCreature) {
        String name = aCreature.getName();
        return name.equals("Ghost Dragon") || name.equals("Archangel") || name.equals("Efreeti") || name.equals("Gargoyle");
    }

    protected boolean shouldIgnore(Creature aCreature) {
        return isNegative() && canFly(aCreature);
    }



//    public static boolean canCreaturePassSpecialField() {
//        String name = stats.getName();
//        boolean canFly = SpecialField.canFly(name);
//        return canFly;
//
//    }
}
