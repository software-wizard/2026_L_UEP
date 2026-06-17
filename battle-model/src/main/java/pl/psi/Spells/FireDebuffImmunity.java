package pl.psi.Spells;

public class FireDebuffImmunity implements SpellImmunity {
    @Override
    public boolean isImmune(Spell spell) {
        return spell.getSchool() == SpellSchool.FIRE && spell instanceof DebuffSpell;
    }
}
