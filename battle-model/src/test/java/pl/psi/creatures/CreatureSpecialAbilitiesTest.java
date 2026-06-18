package pl.psi.creatures;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CreatureSpecialAbilitiesTest {

    @Test
    @DisplayName("Dread Knight powinien zadać podwójne obrażenia, gdy aktywuje się jego zdolność specjalna")
    void dreadKnightShouldDealDoubleDamage() {

        Creature dreadKnight = new Creature(CreatureStatistic.DREAD_KNIGHT);
        Creature defender = new Creature(CreatureStatistic.GHOST_DRAGON);
        int defenderInitialHp = defender.getCurrentHp();

        dreadKnight.SetNextAttackCritical(true); // metoda pomocnicza 

        // Standardowe obrażenia Dread Knighta to Range.closed(15, 30). 
        // Przy podwójnych obrażeniach (bez uwzględniania modyfikatorów Atak/Obrona) bazowe widełki to 30 - 60.

        // When
        dreadKnight.attack(defender);
        int damageDealt = defenderInitialHp - defender.getCurrentHp();

        // Then
        assertThat(damageDealt)
                .as("Obrażenia powinny być wyraźnie wyższe niż standardowe maksimum z powodu krytyka")
                .isGreaterThan(30);
    }

    @Test
    @DisplayName("Zombie powinien nałożyć debuff choroby obniżający statystyki ofiary")
    void zombieAttackShouldApplyDiseaseDebuff() {
        // Given
        Creature zombie = new Creature(CreatureStatistic.ZOMBIE);
        Creature defender = new Creature(CreatureStatistic.WALKING_DEAD);
        
        int initialAttack = defender.getAttack();
        int initialArmor = defender.getArmor();

        zombie.setNextAttackSpecialEffectGuaranteed(true); 

        // When
        zombie.attack(defender);

        // Then
        //"Disease (-2Att -2Def for 3 rounds)"
        assertThat(defender.getAttack())
                .as("Atak zakażonej jednostki powinien zostać obniżony o 2")
                .isEqualTo(initialAttack - 2);

        assertThat(defender.getArmor())
                .as("Obrona (Armor) zakażonej jednostki powinna zostać obniżona o 2")
                .isEqualTo(initialArmor - 2);
    }


    @Test
    @DisplayName("Wight powinien zregenerować wszystkie utracone punkty HP na początku nowej rundy")
    void wightShouldRegenerateLostHpAtStartOfRound() {
        // Given
        Creature wight = new Creature(CreatureStatistic.WIGHT);
        Creature skeleton = new Creature(CreatureStatistic.SKELETON);

        int maxHp = wight.getMaxHp(); // 18

        // Szkielet atakuje Wighta, zadając mu kontrolowane obrażenia (np. 5 HP)
        skeleton.attack(wight);
        
        assertThat(wight.getCurrentHp())
                .as("Wight powinien najpierw otrzymać obrażenia i mieć mniej HP niż wynosi jego maksimum")
                .isLessThan(maxHp);

        // When
        // Symulujemy przejście do nowej rundy walki, co powinno wywołać 
        // regułę specjalną
        wight.applyStartOfRoundEffects(); 

        // Then
        assertThat(wight.getCurrentHp())
                .as("Na początku nowej rundy Wight powinien odzyskać zdrowie do maksymalnego poziomu")
                .isEqualTo(maxHp);
    }
}