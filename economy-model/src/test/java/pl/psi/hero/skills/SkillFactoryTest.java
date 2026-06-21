package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;
import pl.psi.hero.skills.impl.OffenceSkill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillFactoryTest {
    private final SkillFactory skillFactory = new SkillFactory();

    @Test
    void createsImplementedSkill() {
        AbstractSkill skill = skillFactory.create(SkillName.OFFENCE);

        assertTrue(skill instanceof OffenceSkill);
        assertEquals(SkillName.OFFENCE, skill.getName());
        assertEquals(SkillLevel.BASIC, skill.getLevel());
    }

    @Test
    void createsNamedSkillForSkillWithoutImplementation() {
        AbstractSkill skill = skillFactory.create(SkillName.NECROMANCY);

        assertTrue(skill instanceof NamedSkill);
        assertEquals(SkillName.NECROMANCY, skill.getName());
        assertEquals(SkillLevel.BASIC, skill.getLevel());
    }

    @Test
    void createsSkillAtSelectedLevel() {
        AbstractSkill skill = skillFactory.create(SkillName.OFFENCE, SkillLevel.ADVANCED);

        assertTrue(skill instanceof OffenceSkill);
        assertEquals(SkillLevel.ADVANCED, skill.getLevel());
    }

    @Test
    void createsNamedSkillAtSelectedLevel() {
        AbstractSkill skill = skillFactory.create(SkillName.NECROMANCY, SkillLevel.ADVANCED);

        assertTrue(skill instanceof NamedSkill);
        assertEquals(SkillName.NECROMANCY, skill.getName());
        assertEquals(SkillLevel.ADVANCED, skill.getLevel());
    }
}
