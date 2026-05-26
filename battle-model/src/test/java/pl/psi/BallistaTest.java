package pl.psi;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatisticIf;
import com.google.common.collect.Range;

class BallistaTest {

    @Test
    void heroShouldBeAbleToHaveBallistaSkillLevel() {

        Hero hero = new Hero(new ArrayList<>(), new ArrayList<>());

        assertEquals(0, hero.getSkillLevel("ARTILLERY"));

        hero.setSkillLevel("ARTILLERY", 3);
        assertEquals(3, hero.getSkillLevel("ARTILLERY"));
    }

    @Test
    void ballistaShouldDealDamageToTargetCreature() {

        Hero hero = new Hero(new ArrayList<>(), new ArrayList<>());
        Ballista ballista = new Ballista(hero);

        CreatureStatisticIf testStats = new CreatureStatisticIf() {
            @Override public String getName() { return "Test Dummy"; }
            @Override public int getAttack() { return 10; }
            @Override public int getArmor() { return 10; }
            @Override public int getMaxHp() { return 100; }
            @Override public int getMoveRange() { return 5; }
            @Override public Range<Integer> getDamage() { return Range.closed(1, 1); }
            @Override public int getTier() { return 1; }
            @Override public String getDescription() { return ""; }

            @Override public boolean isUpgraded() { return false; }
        };

        Creature targetCreature = new Creature.Builder()
                .statistic(testStats)
                .amount(1)
                .build();

        ballista.attack(targetCreature);

        assertEquals(70, targetCreature.getCurrentHp());
    }
}