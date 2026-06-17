package pl.psi.Spells;

import lombok.Getter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;
import pl.psi.creatures.CreatureStats;


@Getter
public class DebuffSpell extends Spell {

    private final CreatureStats debuffStats;


    public DebuffSpell(String name, int spellLevel, int duration, CreatureStats debuffStats, SpellSchool school) {
        super(name, spellLevel, duration, school);
        this.debuffStats = debuffStats;
    }

    public DebuffSpell(String name, int spellLevel, int duration, CreatureStats debuffStats) {
        this(name, spellLevel, duration, debuffStats, SpellSchool.NONE);
    }

    public DebuffSpell(String name, int spellLevel, int duration, CreatureStats debuffStats, SpellAreaIf areaStrategy, SpellSchool school) {
        super(name, spellLevel, duration, areaStrategy, school);
        this.debuffStats = debuffStats;
    }

    public DebuffSpell(String name, int spellLevel, int duration, CreatureStats debuffStats, SpellAreaIf areaStrategy) {
        this(name, spellLevel, duration, debuffStats, areaStrategy, SpellSchool.NONE);
    }

    @Override
    public void cast(Creature targetCreature, int spellPower) {
        // In Heroes 3, spell power extends debuff duration
        int effectiveDuration = this.getDuration() + spellPower;
        targetCreature.applySpellEffect(this, effectiveDuration);
    }

    @Override
    public CreatureStatisticIf modifyStats(CreatureStatisticIf base) {
        int attackBonus = -debuffStats.getAttack();
        if (attackBonus < 0) {
            attackBonus = Math.max(1, base.getAttack() + attackBonus) - base.getAttack();
        }

        int armorBonus = -debuffStats.getArmor();
        if (armorBonus < 0) {
            armorBonus = Math.max(1, base.getArmor() + armorBonus) - base.getArmor();
        }

        int maxHpBonus = -debuffStats.getMaxHp();
        if (maxHpBonus < 0) {
            maxHpBonus = Math.max(1, base.getMaxHp() + maxHpBonus) - base.getMaxHp();
        }

        int moveRangeBonus = -debuffStats.getMoveRange();
        if (moveRangeBonus < 0) {
            moveRangeBonus = Math.max(1, base.getMoveRange() + moveRangeBonus) - base.getMoveRange();
        }

        return new CreatureStatisticDecorator(
                base,
                attackBonus,
                armorBonus,
                maxHpBonus,
                moveRangeBonus
        );
    }
}
