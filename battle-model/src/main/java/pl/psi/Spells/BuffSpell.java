package pl.psi.Spells;

import lombok.Getter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;
import pl.psi.creatures.CreatureStats;


@Getter
public class BuffSpell extends Spell {

    private final CreatureStats buffStats;


    public BuffSpell(String name, int spellLevel, int duration, CreatureStats buffStats) {
        super(name, spellLevel, duration);
        this.buffStats = buffStats;
    }

    public BuffSpell(String name, int spellLevel, int duration, CreatureStats buffStats, SpellAreaIf areaStrategy) {
        super(name, spellLevel, duration, areaStrategy);
        this.buffStats = buffStats;
    }

    @Override
    public void cast(Creature targetCreature, int spellPower) {
        // In Heroes 3, spell power extends buff duration
        int effectiveDuration = this.getDuration() + spellPower;
        targetCreature.applySpellEffect(this, effectiveDuration);
    }

    @Override
    public CreatureStats modifyStats(CreatureStatisticIf base) {
        return CreatureStats.builder()
                .attack(base.getAttack() + buffStats.getAttack())
                .armor(base.getArmor() + buffStats.getArmor())
                .maxHp(base.getMaxHp() + buffStats.getMaxHp())
                .moveRange(base.getMoveRange() + buffStats.getMoveRange())
                .name(base.getName())
                .description(base.getDescription())
                .tier(base.getTier())
                .damage(base.getDamage())
                .isUpgraded(base.isUpgraded())
                .build();


    }
}