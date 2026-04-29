package pl.psi.creatures;

import com.google.common.collect.Range;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CreatureStatisticIf Interface Tests")
class CreatureStatisticIfTest {

    @Test
    @DisplayName("Should allow enum implementation of interface")
    void testEnumImplementsInterface() {
        CreatureStatisticIf skeleton = CreatureStatistic.SKELETON;
        
        assertThat(skeleton).isInstanceOf(CreatureStatisticIf.class);
        assertThat(skeleton.getName()).isEqualTo("Skeleton");
    }

    @Test
    @DisplayName("Should allow class implementation of interface")
    void testClassImplementsInterface() {
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

        CreatureStatisticIf creature = stats;
        
        assertThat(creature).isInstanceOf(CreatureStatisticIf.class);
        assertThat(creature.getName()).isEqualTo("Test");
    }

    @Test
    @DisplayName("Should provide polymorphic behavior")
    void testPolymorphicBehavior() {
        CreatureStatisticIf enumCreature = CreatureStatistic.VAMPIRE;
        CreatureStatisticIf classCreature = CreatureStats.builder()
            .name("Vampire")
            .attack(9)
            .armor(9)
            .maxHp(30)
            .moveRange(6)
            .damage(Range.closed(5, 8))
            .tier(4)
            .description("Test Vampire")
            .isUpgraded(false)
            .build();

        assertThat(enumCreature.getName()).isEqualTo("Vampire");
        assertThat(classCreature.getName()).isEqualTo("Vampire");
        assertThat(enumCreature.getTier()).isEqualTo(classCreature.getTier());
    }
}