package pl.psi.Spells;

import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;
import pl.psi.creatures.CreatureStats;


@Getter
public class DebuffSpell extends Spell {

    private CreatureStats debuffStats;

    public DebuffSpell(String name, int spellLevel, int duration, CreatureStats debuffStats) {
        super(name, spellLevel, duration);
        this.debuffStats = debuffStats;
    }
    public DebuffSpell() {}

    public DebuffSpell(String name, int spellLevel, int duration, CreatureStats debuffStats, SpellAreaIf areaStrategy) {
        super(name, spellLevel, duration, areaStrategy);
        this.debuffStats = debuffStats;
    }

    @Override
    public void cast(Creature targetCreature, int spellPower) {
        // In Heroes 3, spell power extends debuff duration
        int effectiveDuration = this.getDuration() + spellPower;
        targetCreature.applySpellEffect(this, effectiveDuration);
    }

    @Override
    public CreatureStats modifyStats(CreatureStatisticIf base) {
        return CreatureStats.builder()
                .attack(base.getAttack() - debuffStats.getAttack())
                .armor(base.getArmor() - debuffStats.getArmor())
                .maxHp(base.getMaxHp() - debuffStats.getMaxHp())
                .moveRange(base.getMoveRange() - debuffStats.getMoveRange())
                .name(base.getName())
                .description(base.getDescription())
                .tier(base.getTier())
                .damage(base.getDamage())
                .isUpgraded(base.isUpgraded())
                .build();
    }
}
