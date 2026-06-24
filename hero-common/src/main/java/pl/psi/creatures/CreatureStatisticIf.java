package pl.psi.creatures;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Range;

@JsonDeserialize(using = CreatureStatisticIfDeserializer.class)
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
