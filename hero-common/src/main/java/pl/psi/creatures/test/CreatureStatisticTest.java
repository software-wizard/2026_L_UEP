package pl.psi.creatures;

import com.google.common.collect.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CreatureStatistic Enum Tests")
class CreatureStatisticTest {

    @Test
    @DisplayName("Should create SKELETON with correct stats")
    void testSkeletonStats() {
        CreatureStatistic skeleton = CreatureStatistic.SKELETON;
        
        assertThat(skeleton.getName()).isEqualTo("Skeleton");
        assertThat(skeleton.getAttack()).isEqualTo(4);
        assertThat(skeleton.getArmor()).isEqualTo(6);
        assertThat(skeleton.getMaxHp()).isEqualTo(4);
        assertThat(skeleton.getMoveRange()).isEqualTo(1);
        assertThat(skeleton.getDamage()).isEqualTo(Range.closed(1, 3));
        assertThat(skeleton.getTier()).isEqualTo(1);
        assertThat(skeleton.isUpgraded()).isFalse();
    }

    @Test
    @DisplayName("Should have all creatures in correct tier order")
    void testTierOrdering() {
        assertThat(CreatureStatistic.SKELETON.getTier()).isEqualTo(1);
        assertThat(CreatureStatistic.WALKING_DEAD.getTier()).isEqualTo(2);
        assertThat(CreatureStatistic.WIGHT.getTier()).isEqualTo(3);
        assertThat(CreatureStatistic.VAMPIRE.getTier()).isEqualTo(4);
        assertThat(CreatureStatistic.LICH.getTier()).isEqualTo(5);
        assertThat(CreatureStatistic.BLACK_KNIGHT.getTier()).isEqualTo(6);
        assertThat(CreatureStatistic.BONE_DRAGON.getTier()).isEqualTo(7);
    }

    @Test
    @DisplayName("Should verify upgraded creatures have higher stats than base variants")
    void testUpgradedCreaturesHaveHigherStats() {
        assertThat(CreatureStatistic.SKELETON_WARRIOR.getAttack())
            .isGreaterThanOrEqualTo(CreatureStatistic.SKELETON.getAttack());
        assertThat(CreatureStatistic.VAMPIRE_LORD.getMaxHp())
            .isGreaterThan(CreatureStatistic.VAMPIRE.getMaxHp());
        assertThat(CreatureStatistic.GHOST_DRAGON.getAttack())
            .isGreaterThan(CreatureStatistic.BONE_DRAGON.getAttack());
    }

    @Test
    @DisplayName("Should mark upgraded creatures correctly")
    void testUpgradedCreatureFlag() {
        assertThat(CreatureStatistic.SKELETON.isUpgraded()).isFalse();
        assertThat(CreatureStatistic.SKELETON_WARRIOR.isUpgraded()).isTrue();
        
        assertThat(CreatureStatistic.VAMPIRE.isUpgraded()).isFalse();
        assertThat(CreatureStatistic.VAMPIRE_LORD.isUpgraded()).isTrue();
    }

    @Test
    @DisplayName("Should have non-null descriptions for all creatures")
    void testAllCreaturesHaveDescriptions() {
        for (CreatureStatistic creature : CreatureStatistic.values()) {
            assertThat(creature.getDescription())
                .as("Description for %s", creature.getName())
                .isNotNull()
                .isNotEmpty();
        }
    }

    @Test
    @DisplayName("Should have damage range with valid endpoints")
    void testDamageRangeValidity() {
        for (CreatureStatistic creature : CreatureStatistic.values()) {
            Range<Integer> damage = creature.getDamage();
            assertThat(damage.lowerEndpoint())
                .as("Lower damage for %s", creature.getName())
                .isGreaterThan(0);
            assertThat(damage.upperEndpoint())
                .as("Upper damage for %s", creature.getName())
                .isGreaterThanOrEqualTo(damage.lowerEndpoint());
        }
    }

    @Test
    @DisplayName("Should allow setting attack value")
    void testSetAttack() {
        CreatureStatistic skeleton = CreatureStatistic.SKELETON;
        int originalAttack = skeleton.getAttack();
        
        skeleton.setAttack(10);
        assertThat(skeleton.getAttack()).isEqualTo(10);
        
        // Restore original
        skeleton.setAttack(originalAttack);
    }

    @Test
    @DisplayName("Should allow setting armor value")
    void testSetArmor() {
        CreatureStatistic skeleton = CreatureStatistic.SKELETON;
        int originalArmor = skeleton.getArmor();
        
        skeleton.setArmor(12);
        assertThat(skeleton.getArmor()).isEqualTo(12);
        
        // Restore original
        skeleton.setArmor(originalArmor);
    }

    @Test
    @DisplayName("Should have BONE_DRAGON with highest tier")
    void testBoneDragonIsHighestTier() {
        assertThat(CreatureStatistic.BONE_DRAGON.getTier()).isEqualTo(7);
        assertThat(CreatureStatistic.GHOST_DRAGON.getTier()).isEqualTo(7);
        
        for (CreatureStatistic creature : CreatureStatistic.values()) {
            assertThat(creature.getTier()).isLessThanOrEqualTo(7);
        }
    }

    @Test
    @DisplayName("Should have all creatures with positive stats")
    void testAllCreaturesHavePositiveStats() {
        for (CreatureStatistic creature : CreatureStatistic.values()) {
            assertThat(creature.getAttack()).isPositive();
            assertThat(creature.getArmor()).isPositive();
            assertThat(creature.getMaxHp()).isPositive();
            assertThat(creature.getMoveRange()).isPositive();
            assertThat(creature.getTier()).isPositive();
        }
    }
}