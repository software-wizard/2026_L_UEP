package pl.psi.warmachines;

import com.google.common.collect.Range;
import pl.psi.creatures.CreatureStatisticIf;

public enum WarMachineStats implements CreatureStatisticIf {

    BALLISTA("Ballista", 20, 5, 250, 0, Range.closed(20, 30), 1, "Shoots enemies.", false),
    MEDIC_TENT("First Aid Tent", 0, 0, 75, 0, Range.closed(0, 0), 1, "Heals allies.", false),
    AMMO_CART("Ammo Cart", 0, 0, 50, 0, Range.closed(0, 0), 1, "Provides endless ammo.", false);

    private final String name;
    private final int attack;
    private final int armor;
    private final int maxHp;
    private final int moveRange;
    private final Range<Integer> damage;
    private final int tier;
    private final String description;
    private final boolean isUpgraded;

    WarMachineStats(String name, int attack, int armor, int maxHp, int moveRange, Range<Integer> damage, int tier, String description, boolean isUpgraded) {
        this.name = name;
        this.attack = attack;
        this.armor = armor;
        this.maxHp = maxHp;
        this.moveRange = moveRange;
        this.damage = damage;
        this.tier = tier;
        this.description = description;
        this.isUpgraded = isUpgraded;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAttack() {
        return attack;
    }

    @Override
    public int getArmor() {
        return armor;
    }

    @Override
    public int getMaxHp() {
        return maxHp;
    }

    @Override
    public int getMoveRange() {
        return moveRange;
    }

    @Override
    public Range<Integer> getDamage() {
        return damage;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean isUpgraded() {
        return isUpgraded;
    }
}