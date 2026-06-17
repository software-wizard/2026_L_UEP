package pl.psi.Spells;

import lombok.Getter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;
import pl.psi.creatures.CreatureStats;


@Getter
public class BuffSpell extends Spell {

    private final CreatureStats buffStats;


    public BuffSpell(String name, int spellLevel, int duration, CreatureStats buffStats, SpellSchool school) {
        super(name, spellLevel, duration, school);
        this.buffStats = buffStats;
    }

    public BuffSpell(String name, int spellLevel, int duration, CreatureStats buffStats) {
        this(name, spellLevel, duration, buffStats, SpellSchool.NONE);
    }

    public BuffSpell(String name, int spellLevel, int duration, CreatureStats buffStats, SpellAreaIf areaStrategy, SpellSchool school) {
        super(name, spellLevel, duration, areaStrategy, school);
        this.buffStats = buffStats;
    }

    public BuffSpell(String name, int spellLevel, int duration, CreatureStats buffStats, SpellAreaIf areaStrategy) {
        this(name, spellLevel, duration, buffStats, areaStrategy, SpellSchool.NONE);
    }

    @Override
    public void cast(Creature targetCreature, int spellPower) {
        // In Heroes 3, spell power extends buff duration
        int effectiveDuration = this.getDuration() + spellPower;
        targetCreature.applySpellEffect(this, effectiveDuration);
    }

    @Override
    public CreatureStatisticIf modifyStats(CreatureStatisticIf base) {
        return new CreatureStatisticDecorator(
                base,
                buffStats.getAttack(),
                buffStats.getArmor(),
                buffStats.getMaxHp(),
                buffStats.getMoveRange()
        );
    }
}