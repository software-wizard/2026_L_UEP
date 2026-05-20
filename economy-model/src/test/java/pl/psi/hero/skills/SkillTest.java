package pl.psi.hero.skills;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillTest {
    @Test
    void testOffenceSkillUpgrade() {
        OffenceSkill offenceSkill = new OffenceSkill();

        assertEquals(SkillLevel.BASIC, offenceSkill.getLevel());
        assertEquals(0.1f, offenceSkill.getFactor(), 0.01);

        offenceSkill.upgrade();
        assertEquals(SkillLevel.ADVANCED, offenceSkill.getLevel());
        assertEquals(0.2f, offenceSkill.getFactor(), 0.01);

        offenceSkill.upgrade();
        assertEquals(SkillLevel.EXPERT, offenceSkill.getLevel());
        assertEquals(0.3f, offenceSkill.getFactor(), 0.01);

        assertThrows(IllegalStateException.class, offenceSkill::upgrade);
    }
    @Test
    void testArmorerSkillUpgrade() {
        ArmorerSkill armorerSkill = new ArmorerSkill();

        assertEquals(SkillLevel.BASIC, armorerSkill.getLevel());
        assertEquals(0.1f, armorerSkill.getFactor(), 0.01);

        armorerSkill.upgrade();
        assertEquals(SkillLevel.ADVANCED, armorerSkill.getLevel());
        assertEquals(0.2f, armorerSkill.getFactor(), 0.01);

        armorerSkill.upgrade();
        assertEquals(SkillLevel.EXPERT, armorerSkill.getLevel());
        assertEquals(0.3f, armorerSkill.getFactor(), 0.01);

        assertThrows(IllegalStateException.class, armorerSkill::upgrade);
    }
    @Test
    void testLearningSkillUpgrade() {
        LearningSkill learningSkill = new LearningSkill();

        assertEquals(SkillLevel.BASIC, learningSkill.getLevel());
        assertEquals(0.05f, learningSkill.getFactor(), 0.01);

        learningSkill.upgrade();
        assertEquals(SkillLevel.ADVANCED, learningSkill.getLevel());
        assertEquals(0.10f, learningSkill.getFactor(), 0.01);

        learningSkill.upgrade();
        assertEquals(SkillLevel.EXPERT, learningSkill.getLevel());
        assertEquals(0.15f, learningSkill.getFactor(), 0.01);

        assertThrows(IllegalStateException.class, learningSkill::upgrade);
    }
    @Test
    void testLogisticsSkillUpgrade() {
        LogisticsSkill logisticsSkill = new LogisticsSkill();

        assertEquals(SkillLevel.BASIC, logisticsSkill.getLevel());
        assertEquals(0.1f, logisticsSkill.getFactor(), 0.01);

        logisticsSkill.upgrade();
        assertEquals(SkillLevel.ADVANCED, logisticsSkill.getLevel());
        assertEquals(0.2f, logisticsSkill.getFactor(), 0.01);

        logisticsSkill.upgrade();
        assertEquals(SkillLevel.EXPERT, logisticsSkill.getLevel());
        assertEquals(0.3f, logisticsSkill.getFactor(), 0.01);

        assertThrows(IllegalStateException.class, logisticsSkill::upgrade);
    }
    @Test
    void testTacticsSkillUpgrade() {
        TacticsSkill tacticsSkill = new TacticsSkill();

        assertEquals(SkillLevel.BASIC, tacticsSkill.getLevel());
        assertEquals(1.0f, tacticsSkill.getFactor(), 0.01);

        tacticsSkill.upgrade();
        assertEquals(SkillLevel.ADVANCED, tacticsSkill.getLevel());
        assertEquals(2.0f, tacticsSkill.getFactor(), 0.01);

        tacticsSkill.upgrade();
        assertEquals(SkillLevel.EXPERT, tacticsSkill.getLevel());
        assertEquals(3.0f, tacticsSkill.getFactor(), 0.01);

        assertThrows(IllegalStateException.class, tacticsSkill::upgrade);
    }
    @Test
    void testAirMagicSkillUpgrade() {
        AirMagicSkill skill = new AirMagicSkill();
        assertEquals(SkillLevel.BASIC, skill.getLevel());
        assertEquals(0.1f, skill.getFactor(), 0.01);
        skill.upgrade();
        assertEquals(SkillLevel.ADVANCED, skill.getLevel());
        assertEquals(0.2f, skill.getFactor(), 0.01);
        skill.upgrade();
        assertEquals(SkillLevel.EXPERT, skill.getLevel());
        assertEquals(0.3f, skill.getFactor(), 0.01);
        assertThrows(IllegalStateException.class, skill::upgrade);
    }
    @Test
    void testEarthMagicSkillUpgrade() {
        EarthMagicSkill skill = new EarthMagicSkill();
        assertEquals(SkillLevel.BASIC, skill.getLevel());
        assertEquals(0.1f, skill.getFactor(), 0.01);
        skill.upgrade();
        assertEquals(SkillLevel.ADVANCED, skill.getLevel());
        assertEquals(0.2f, skill.getFactor(), 0.01);
        skill.upgrade();
        assertEquals(SkillLevel.EXPERT, skill.getLevel());
        assertEquals(0.3f, skill.getFactor(), 0.01);
        assertThrows(IllegalStateException.class, skill::upgrade);
    }
    @Test
    void testFireMagicSkillUpgrade() {
        FireMagicSkill skill = new FireMagicSkill();
        assertEquals(SkillLevel.BASIC, skill.getLevel());
        assertEquals(0.1f, skill.getFactor(), 0.01);
        skill.upgrade();
        assertEquals(SkillLevel.ADVANCED, skill.getLevel());
        assertEquals(0.2f, skill.getFactor(), 0.01);
        skill.upgrade();
        assertEquals(SkillLevel.EXPERT, skill.getLevel());
        assertEquals(0.3f, skill.getFactor(), 0.01);
        assertThrows(IllegalStateException.class, skill::upgrade);
    }
    @Test
    void testWaterMagicSkillUpgrade() {
        WaterMagicSkill skill = new WaterMagicSkill();
        assertEquals(SkillLevel.BASIC, skill.getLevel());
        assertEquals(0.1f, skill.getFactor(), 0.01);
        skill.upgrade();
        assertEquals(SkillLevel.ADVANCED, skill.getLevel());
        assertEquals(0.2f, skill.getFactor(), 0.01);
        skill.upgrade();
        assertEquals(SkillLevel.EXPERT, skill.getLevel());
        assertEquals(0.3f, skill.getFactor(), 0.01);
        assertThrows(IllegalStateException.class, skill::upgrade);
    }
}
