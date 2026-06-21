package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.impl.LearningSkill;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LearningSkillExperienceTest {

    @Test
    void experienceWithoutLearningIsUnchanged() {
        EconomyHero hero = new EconomyHero();
        hero.setExperience(0);
        hero.setLevel(1);

        hero.addExperience(100);

        assertEquals(100, hero.getExperience());
    }

    @Test
    void basicLearningAddsFivePercentExperience() {
        EconomyHero hero = new EconomyHero();
        hero.setExperience(0);
        hero.setLevel(1);
        hero.upgradeSkill(new LearningSkill());

        hero.addExperience(100);

        assertEquals(105, hero.getExperience());
    }

    @Test
    void advancedLearningAddsTenPercentExperience() {
        EconomyHero hero = new EconomyHero();
        hero.setExperience(0);
        hero.setLevel(1);
        hero.upgradeSkill(new LearningSkill());
        hero.upgradeSkill(new LearningSkill());

        hero.addExperience(100);

        assertEquals(110, hero.getExperience());
    }

    @Test
    void expertLearningAddsFifteenPercentExperience() {
        EconomyHero hero = new EconomyHero();
        hero.setExperience(0);
        hero.setLevel(1);
        hero.upgradeSkill(new LearningSkill());
        hero.upgradeSkill(new LearningSkill());
        hero.upgradeSkill(new LearningSkill());

        hero.addExperience(100);

        assertEquals(115, hero.getExperience());
    }

    @Test
    void learningBonusIsNotAppliedTwice() {
        EconomyHero hero = new EconomyHero();
        hero.setExperience(0);
        hero.setLevel(1);
        hero.upgradeSkill(new LearningSkill());

        hero.addExperience(100);

        assertEquals(105, hero.getExperience());
    }
}
