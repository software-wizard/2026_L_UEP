package pl.psi.creatures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CreatureRetaliationTest {

    private Creature skeleton;
    private Creature vampire;
    private Creature walkingDeadDefender1;
    private Creature walkingDeadDefender2;

    @BeforeEach
    void setUp() {
        skeleton = new Creature(CreatureStatistic.SKELETON);
        vampire = new Creature(CreatureStatistic.VAMPIRE);

        walkingDeadDefender1 = new Creature(CreatureStatistic.WALKING_DEAD);
        walkingDeadDefender2 = new Creature(CreatureStatistic.WALKING_DEAD);
    }

    @Test
    @DisplayName("Szkielet powinien otrzymać kontratak po zaatakowaniu Walking Dead")
    void skeletonShouldReceiveRetaliation() {
        // Given
        int skeletonInitialHp = skeleton.getCurrentHp();
        
        // When
        skeleton.attack(walkingDeadDefender1);

        // Then
        // Szkielet atakuje -> Walking Dead traci HP -> Walking Dead kontratakuje -> Szkielet traci HP
        assertThat(skeleton.getCurrentHp())
                .as("Szkielet powinien mieć mniej HP niż na początku, ponieważ obrońca skontratakował")
                .isLessThan(skeletonInitialHp);
    }

    @Test
    @DisplayName("Wampir NIE powinien otrzymać kontrataku po zaatakowaniu Walking Dead")
    void vampireShouldNotReceiveRetaliation() {
        // Given
        int vampireInitialHp = vampire.getCurrentHp();

        // When
        vampire.attack(walkingDeadDefender2);

        // Then
        // Wampir atakuje -> Walking Dead traci HP -> Brak kontrataku ze strony Walking Dead
        assertThat(vampire.getCurrentHp())
                .as("Wampir powinien zachować pełne HP, ponieważ posiada cechę braku kontrataku wroga")
                .isEqualTo(vampireInitialHp);
    }
}