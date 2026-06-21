package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NamedSkillTest {

    @Test
    void necromancyCanExistAtBasicLevel() {
        NamedSkill skill = new NamedSkill(SkillName.NECROMANCY, SkillLevel.BASIC);

        assertEquals(SkillName.NECROMANCY, skill.getName());
        assertEquals(SkillLevel.BASIC, skill.getLevel());
    }

    @Test
    void necromancyUpgradesFromBasicToAdvancedAndExpert() {
        NamedSkill skill = new NamedSkill(SkillName.NECROMANCY, SkillLevel.BASIC);

        skill.upgrade();
        assertEquals(SkillLevel.ADVANCED, skill.getLevel());

        skill.upgrade();
        assertEquals(SkillLevel.EXPERT, skill.getLevel());
    }

    @Test
    void upgradingExpertNamedSkillKeepsExpertLevel() {
        NamedSkill skill = new NamedSkill(SkillName.NECROMANCY, SkillLevel.EXPERT);

        skill.upgrade();

        assertEquals(SkillLevel.EXPERT, skill.getLevel());
    }
}
