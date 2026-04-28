package pl.psi.hero;

import org.junit.jupiter.api.Test;
import pl.psi.map.resources.Resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EconomyHeroExperienceTest {

    private final Statistics stats = new Statistics(1, 1, 1, 1);

    @Test
    void shouldInitializeHeroExperienceInExpectedRange() {
        EconomyHero hero = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, new Resources(1000, 0, 0, 0, 0, 0, 0), stats);
        assertTrue(hero.getExperience() >= 40 && hero.getExperience() <= 90);
    }

    @Test
    void shouldRequireHomm3ThresholdToReachSecondLevel() {
        EconomyHero hero = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, new Resources(1000, 0, 0, 0, 0, 0, 0), stats);

        int toSecondLevel = 1000 - hero.getExperience();
        hero.addExperience(Math.max(0, toSecondLevel - 1));
        assertEquals(0, hero.level);

        hero.addExperience(1);
        assertEquals(1, hero.level);
    }
}
