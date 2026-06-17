package pl.psi.Spells;

import com.google.common.collect.Range;
import pl.psi.creatures.CreatureStatisticIf;

public class CreatureStatisticDecorator implements CreatureStatisticIf {
    private final CreatureStatisticIf base;
    private final int attackBonus;
    private final int armorBonus;
    private final int maxHpBonus;
    private final int moveRangeBonus;

    public CreatureStatisticDecorator(CreatureStatisticIf base, int attackBonus, int armorBonus, int maxHpBonus, int moveRangeBonus) {
        this.base = base;
        this.attackBonus = attackBonus;
        this.armorBonus = armorBonus;
        this.maxHpBonus = maxHpBonus;
        this.moveRangeBonus = moveRangeBonus;
    }

    @Override
    public String getName() {
        return base.getName();
    }

    @Override
    public int getAttack() {
        return base.getAttack() + attackBonus;
    }

    @Override
    public int getArmor() {
        return base.getArmor() + armorBonus;
    }

    @Override
    public int getMaxHp() {
        return base.getMaxHp() + maxHpBonus;
    }

    @Override
    public int getMoveRange() {
        return base.getMoveRange() + moveRangeBonus;
    }

    @Override
    public Range<Integer> getDamage() {
        return base.getDamage();
    }

    @Override
    public int getTier() {
        return base.getTier();
    }

    @Override
    public String getDescription() {
        return base.getDescription();
    }

    @Override
    public boolean isUpgraded() {
        return base.isUpgraded();
    }
}
