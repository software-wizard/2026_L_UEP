package pl.psi.Spells;

import pl.psi.creatures.Creature;

public class DamageSpell extends Spell{

    public DamageSpell(String name, int spellLevel, int duration) {
        super(name, spellLevel, duration);
    }

    public DamageSpell(String name, int spellLevel, int duration, SpellAreaIf areaStrategy) {
        super(name, spellLevel, duration, areaStrategy);
    }
    public DamageSpell() {}

    @Override
    public void cast(Creature targetCreature, int spellPower) {
        targetCreature.applyMagicDamage(this, spellPower);
    }


}
