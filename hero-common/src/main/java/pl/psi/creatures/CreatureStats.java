package pl.psi.creatures;

import com.google.common.collect.Range;

import lombok.*;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CreatureStats implements CreatureStatisticIf{
    private String name;
    private int attack;
    private int armor;
    private int maxHp;
    private int moveRange;
    private Range< Integer > damage;
    private int tier;
    private String description;
    private boolean isUpgraded;

    public CreatureStats(CreatureStats other) {
        this.name = other.name;
        this.attack = other.attack;
        this.armor = other.armor;
        this.maxHp = other.maxHp;
        this.moveRange = other.moveRange;
        // Tworzymy nowy obiekt Range, żeby było deep copy
        this.damage = Range.closed(other.damage.lowerEndpoint(), other.damage.upperEndpoint());
        this.tier = other.tier;
        this.description = other.description;
        this.isUpgraded = other.isUpgraded;
    }
}
