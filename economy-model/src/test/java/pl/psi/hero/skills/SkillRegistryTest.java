package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillRegistryTest {
    private final SkillRegistry skillRegistry = new SkillRegistry();

    @Test
    void containsAllAvailableSkillsUsedByChoiceManager() {
        List<SkillName> availableSkills = skillRegistry.getAvailableSkillNames();

        assertTrue(availableSkills.contains(SkillName.OFFENCE));
        assertTrue(availableSkills.contains(SkillName.ARMORER));
        assertTrue(availableSkills.contains(SkillName.LEARNING));
        assertTrue(availableSkills.contains(SkillName.LOGISTICS));
        assertTrue(availableSkills.contains(SkillName.PATHFINDING));
        assertTrue(availableSkills.contains(SkillName.TACTICS));
        assertTrue(availableSkills.contains(SkillName.LEADERSHIP));
        assertTrue(availableSkills.contains(SkillName.LUCK));
        assertTrue(availableSkills.contains(SkillName.AIR_MAGIC));
        assertTrue(availableSkills.contains(SkillName.EARTH_MAGIC));
        assertTrue(availableSkills.contains(SkillName.FIRE_MAGIC));
        assertTrue(availableSkills.contains(SkillName.WATER_MAGIC));
    }

    @Test
    void doesNotContainDuplicateSkillNames() {
        List<SkillName> availableSkills = skillRegistry.getAvailableSkillNames();

        assertEquals(new HashSet<>(availableSkills).size(), availableSkills.size());
    }
}
