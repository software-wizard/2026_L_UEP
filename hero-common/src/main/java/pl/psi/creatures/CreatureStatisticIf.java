package pl.psi.creatures;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.collect.Range;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreatureStatistic.class, name = "CreatureStatistic"),
        @JsonSubTypes.Type(value = CreatureStats.class, name = "CreatureStats")
})
public interface CreatureStatisticIf {
    String getName();
    int getAttack();
    int getArmor();
    int getMaxHp();
    int getMoveRange();
    Range< Integer > getDamage();
    int getTier();
    String getDescription();
    boolean isUpgraded();
}
