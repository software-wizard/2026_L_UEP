package pl.psi.creatures;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Range;
import lombok.Getter;

public class ModifiedCreatureStats implements CreatureStatisticIf {
    @Getter
    @JsonProperty("base")
    private CreatureStatisticIf baseStats;
    @JsonProperty("modifier")
    private StatsModifier bonusStats;

    public ModifiedCreatureStats(CreatureStatisticIf base, StatsModifier bonus) {
        this.baseStats = base;
        this.bonusStats = bonus;
    }
    public ModifiedCreatureStats() {
    }

    @Override
    public int getAttack() {
        return baseStats.getAttack() + bonusStats.getAttackBonus();
    }

    @Override
    public int getArmor() {
        return baseStats.getArmor() + bonusStats.getArmorBonus();
    }

    @Override
    public int getMaxHp() {
        return baseStats.getMaxHp();
    }

    @Override
    public int getMoveRange() {
        return baseStats.getMoveRange();
    }

    @Override
    public Range<Integer> getDamage() {
        return baseStats.getDamage();
    }

    @Override
    public String getName() {
        return baseStats.getName();
    }

    @Override
    public int getTier() {
        return baseStats.getTier();
    }

    @Override
    public String getDescription() {
        return baseStats.getDescription();
    }

    @Override
    public boolean isUpgraded() {
        return baseStats.isUpgraded();
    }
}
