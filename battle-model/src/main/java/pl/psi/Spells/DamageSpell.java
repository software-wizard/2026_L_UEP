package pl.psi.Spells;

import pl.psi.creatures.Creature;

public class DamageSpell extends Spell{

    public DamageSpell(String name, int spellLevel, int duration, SpellSchool school) {
        super(name, spellLevel, duration, school);
    }

    public DamageSpell(String name, int spellLevel, int duration) {
        this(name, spellLevel, duration, SpellSchool.NONE);
    }

    public DamageSpell(String name, int spellLevel, int duration, SpellAreaIf areaStrategy, SpellSchool school) {
        super(name, spellLevel, duration, areaStrategy, school);
    }

    public DamageSpell(String name, int spellLevel, int duration, SpellAreaIf areaStrategy) {
        this(name, spellLevel, duration, areaStrategy, SpellSchool.NONE);
    }

    @Override
    public void cast(Creature targetCreature, int spellPower) {
        targetCreature.applyMagicDamage(this, spellPower);
    }


}
