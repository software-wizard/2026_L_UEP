package pl.psi;


import lombok.Getter;
import lombok.Setter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;

@Getter
@Setter
public abstract class SpecialField {

    public enum Color {
        CYAN, BROWN, ORANGE, YELLOW, GRAY, RED, GREEN, DARKRED, VIOLET, SADDLEBROWN, LIGHTBLUE, TEAL, ORANGERED, GOLD, PURPLE, LIMEGREEN, LIGHTSLATEGREY, CRIMSON, ALICEBLUE, KHAKI
    }

    public enum FieldName{
        DMG_FIELD, SPELL_FIELD, BUFF_FIELD, FIELD_CAN_ONLY_BE_FLOWN, DEBUFF_FIELD, FIRE_FIELD, CRACKED_ICE, QUICKSAND, FIELDS_OF_GLORY, MAGIC_PLAINS
    }

    @Getter
    private FieldName fieldName;

    @Getter
    @Setter
    private Color color = Color.GRAY;

    protected SpecialField(FieldName aFieldName) {
        fieldName = aFieldName;
        // Default color mapping based on FieldName
        if (fieldName != null) {
            switch (fieldName) {
                case DMG_FIELD: color = Color.CRIMSON; break;
                case SPELL_FIELD: color = Color.CYAN; break;
                case BUFF_FIELD: color = Color.ORANGE; break;
                case DEBUFF_FIELD: color = Color.GRAY; break;
                case FIELD_CAN_ONLY_BE_FLOWN: color = Color.DARKRED; break;
                case FIRE_FIELD: color = Color.ORANGE; break;
                case CRACKED_ICE: color = Color.ALICEBLUE; break;
                case QUICKSAND: color = Color.KHAKI; break;
                case FIELDS_OF_GLORY: color = Color.LIGHTSLATEGREY; break;
                case MAGIC_PLAINS: color = Color.VIOLET; break;
                default: color = Color.GRAY; break;
            }
        }
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
