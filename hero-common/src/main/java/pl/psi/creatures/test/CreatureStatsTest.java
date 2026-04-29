package pl.psi.creatures;

import com.google.common.collect.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CreatureStats Class Tests")
class CreatureStatsTest {

    @Test
    @DisplayName("Should create CreatureStats with builder")
    void testBuilderCreation() {
        CreatureStats stats = CreatureStats.builder()
            .name("Test Creature")
            .attack(5)
            .armor(6)
            .maxHp(20)
            .moveRange(4)
            .damage(Range.closed(2, 5))
            .tier(2)
            .description("Test description")
            .isUpgraded(false)
            .build();

        assertThat(stats.getName()).isEqualTo("Test Creature");
        assertThat(stats.getAttack()).isEqualTo(5);
        assertThat(stats.getArmor()).isEqualTo(6);
        assertThat(stats.getMaxHp()).isEqualTo(20);
    }

    @Test
    @DisplayName("Should copy CreatureStats with copy constructor")
    void testCopyConstructor() {
        CreatureStats original = CreatureStats.builder()
            .name("Original")
            .attack(10)
            .armor(8)
            .maxHp(50)
            .moveRange(5)
            .damage(Range.closed(5, 15))
            .tier(3)
            .description("Original description")
            .isUpgraded(true)
            .build();

        CreatureStats copy = new CreatureStats(original);

        assertThat(copy.getName()).isEqualTo(original.getName());
        assertThat(copy.getAttack()).isEqualTo(original.getAttack());
        assertThat(copy.getArmor()).isEqualTo(original.getArmor());
        assertThat(copy.getMaxHp()).isEqualTo(original.getMaxHp());
        assertThat(copy.getMoveRange()).isEqualTo(original.getMoveRange());
        assertThat(copy.getTier()).isEqualTo(original.getTier());
        assertThat(copy.isUpgraded()).isEqualTo(original.isUpgraded());
    }

    @Test
    @DisplayName("Should perform deep copy of damage range")
    void testDeepCopyOfDamageRange() {
        CreatureStats original = CreatureStats.builder()
            .name("Test")
            .attack(5)
            .armor(5)
            .maxHp(20)
            .moveRange(3)
            .damage(Range.closed(3, 8))
            .tier(2)
            .description("Test")
            .isUpgraded(false)
            .build();

        CreatureStats copy = new CreatureStats(original);

        assertThat(copy.getDamage()).isEqualTo(original.getDamage());
        assertThat(copy.getDamage().lowerEndpoint()).isEqualTo(3);
        assertThat(copy.getDamage().upperEndpoint()).isEqualTo(8);
    }

    @Test
    @DisplayName("Should ensure copied objects are independent")
    void testCopyIndependence() {
        CreatureStats original = CreatureStats.builder()
            .name("Original")
            .attack(10)
            .armor(8)
            .maxHp(50)
            .moveRange(5)
            .damage(Range.closed(5, 15))
            .tier(3)
            .description("Original")
            .isUpgraded(false)
            .build();

        CreatureStats copy = new CreatureStats(original);

        // Verify they are different objects
        assertThat(copy).isNotSameAs(original);
        assertThat(copy.getDamage()).isNotSameAs(original.getDamage());
    }

    @Test
    @DisplayName("Should build creature from enum statistic")
    void testBuildFromEnumStatistic() {
        CreatureStats stats = CreatureStats.builder()
            .name(CreatureStatistic.SKELETON.getName())
            .attack(CreatureStatistic.SKELETON.getAttack())
            .armor(CreatureStatistic.SKELETON.getArmor())
            .maxHp(CreatureStatistic.SKELETON.getMaxHp())
            .moveRange(CreatureStatistic.SKELETON.getMoveRange())
            .damage(CreatureStatistic.SKELETON.getDamage())
            .tier(CreatureStatistic.SKELETON.getTier())
            .description(CreatureStatistic.SKELETON.getDescription())
            .isUpgraded(CreatureStatistic.SKELETON.isUpgraded())
            .build();

        assertThat(stats.getName()).isEqualTo("Skeleton");
        assertThat(stats.getTier()).isEqualTo(1);
        assertThat(stats.isUpgraded()).isFalse();
    }

    @Test
    @DisplayName("Should handle upgraded creatures")
    void testUpgradedCreature() {
        CreatureStats stats = CreatureStats.builder()
            .name("Upgraded Skeleton")
            .attack(6)
            .armor(6)
            .maxHp(6)
            .moveRange(5)
            .damage(Range.closed(1, 3))
            .tier(1)
            .description("Upgraded")
            .isUpgraded(true)
            .build();

        assertThat(stats.isUpgraded()).isTrue();
    }

    @Test
    @DisplayName("Should validate all fields are accessible")
    void testAllFieldsAccessible() {
        CreatureStats stats = CreatureStats.builder()
            .name("Test")
            .attack(5)
            .armor(6)
            .maxHp(25)
            .moveRange(4)
            .damage(Range.closed(2, 6))
            .tier(2)
            .description("Test description")
            .isUpgraded(false)
            .build();

        assertThat(stats.getName()).isNotNull();
        assertThat(stats.getAttack()).isGreaterThan(0);
        assertThat(stats.getArmor()).isGreaterThan(0);
        assertThat(stats.getMaxHp()).isGreaterThan(0);
        assertThat(stats.getMoveRange()).isGreaterThan(0);
        assertThat(stats.getDamage()).isNotNull();
        assertThat(stats.getTier()).isGreaterThan(0);
        assertThat(stats.getDescription()).isNotNull();
    }

    @Test
    @DisplayName("Should implement CreatureStatisticIf interface")
    void testImplementsInterface() {
        CreatureStats stats = CreatureStats.builder()
            .name("Test")
            .attack(5)
            .armor(6)
            .maxHp(25)
            .moveRange(4)
            .damage(Range.closed(2, 6))
            .tier(2)
            .description("Test")
            .isUpgraded(false)
            .build();

        assertThat(stats).isInstanceOf(CreatureStatisticIf.class);
    }
}