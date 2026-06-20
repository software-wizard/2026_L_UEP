package pl.psi.creatures;

import com.google.common.collect.Range;

import lombok.*;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description.).
 */
@RequiredArgsConstructor
@Builder
@Getter
public class CreatureStats implements CreatureStatisticIf {
    private final String name;
    private final int attack;
    private final int armor;
    private final int maxHp;
    private final int moveRange;
    private final Range<Integer> damage;
    private final int tier;
    private final String description;
    private final boolean isUpgraded;
    @Builder.Default
    private final MovementType movementType = MovementType.WALKING;
    @Builder.Default
    private final boolean ranged = false;
    @Builder.Default
    private final boolean doubleAttacker = false;

    @Override
    public boolean isRanged() { return ranged; }

    @Override
    public boolean isDoubleAttacker() { return doubleAttacker; }

    public CreatureStats(CreatureStats other) {
        this.name = other.name;
        this.attack = other.attack;
        this.armor = other.armor;
        this.maxHp = other.maxHp;
        this.moveRange = other.moveRange;
        this.damage = Range.closed(other.damage.lowerEndpoint(), other.damage.upperEndpoint());
        this.tier = other.tier;
        this.description = other.description;
        this.isUpgraded = other.isUpgraded;
        this.movementType = other.movementType;
        this.ranged = other.ranged;
        this.doubleAttacker = other.doubleAttacker;
    }
}