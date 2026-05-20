package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;
import pl.psi.hero.EconomyHero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LearningSkillExperienceTest {

    @Test
    void shouldApplyLearningBonusAfterHeroChoosesAndUpgradesLearningSkill() {
        EconomyHero hero = new EconomyHero();

        hero.upgradeSkill(new LearningSkill());

        int experienceBeforeBasicLearning = hero.getExperience();
        hero.addExperience(10);
        assertEquals(experienceBeforeBasicLearning + 11, hero.getExperience());

        hero.upgradeSkill(new LearningSkill());

        int experienceBeforeAdvancedLearning = hero.getExperience();
        hero.addExperience(10);
        assertEquals(experienceBeforeAdvancedLearning + 11, hero.getExperience());

        hero.upgradeSkill(new LearningSkill());

        int experienceBeforeExpertLearning = hero.getExperience();
        hero.addExperience(10);
        assertEquals(experienceBeforeExpertLearning + 12, hero.getExperience());
    }
}