package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.impl.LearningSkill;
import pl.psi.hero.skills.modifiers.ExpModifierIf;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LearningSkillExperienceTest {

    @Test
    void experienceWithoutLearningIsUnchanged() {
        EconomyHero hero = new EconomyHero();

        int experienceBefore = hero.getExperience();
        hero.addExperience(100);

        assertEquals(experienceBefore + 100, hero.getExperience());
    }

    @Test
    void basicLearningAddsFivePercentExperience() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LearningSkill());

        int experienceBefore = hero.getExperience();
        hero.addExperience(100);

        assertEquals(experienceBefore + 105, hero.getExperience());
    }

    @Test
    void advancedLearningAddsTenPercentExperience() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LearningSkill());
        hero.upgradeSkill(new LearningSkill());

        int experienceBefore = hero.getExperience();
        hero.addExperience(100);

        assertEquals(experienceBefore + 110, hero.getExperience());
    }

    @Test
    void expertLearningAddsFifteenPercentExperience() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LearningSkill());
        hero.upgradeSkill(new LearningSkill());
        hero.upgradeSkill(new LearningSkill());

        int experienceBefore = hero.getExperience();
        hero.addExperience(100);

        assertEquals(experienceBefore + 115, hero.getExperience());
    }

    @Test
    void learningBonusIsNotAppliedTwice() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LearningSkill());

        int experienceBefore = hero.getExperience();
        hero.addExperience(100);

        assertEquals(experienceBefore + 105, hero.getExperience());
    }

    @Test
    void customExpModifierStillAffectsAddedExperience() {
        TestEconomyHero hero = new TestEconomyHero();
        hero.addTestExpModifier(() -> 1.25);

        int experienceBefore = hero.getExperience();
        hero.addExperience(100);

        assertEquals(experienceBefore + 125, hero.getExperience());
    }

    private static class TestEconomyHero extends EconomyHero {
        void addTestExpModifier(ExpModifierIf modifier) {
            addExpModifier(modifier);
        }
    }
}
