package pl.psi.Spells;

public class FireMagicImmunity implements SpellImmunity {
    @Override
    public boolean isImmune(Spell spell) {
        return spell.getSchool() == SpellSchool.FIRE;
    }
}
