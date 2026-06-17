package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.impl.EarthMagicSkill;
import pl.psi.hero.skills.impl.AirMagicSkill;
import pl.psi.hero.skills.impl.FireMagicSkill;
import pl.psi.hero.skills.impl.LeadershipSkill;
import pl.psi.hero.skills.impl.LogisticsSkill;
import pl.psi.hero.skills.impl.LuckSkill;
import pl.psi.hero.skills.impl.OffenceSkill;
import pl.psi.hero.skills.impl.TacticsSkill;
import pl.psi.hero.skills.impl.WaterMagicSkill;
import pl.psi.hero.skills.modifiers.MoraleModifierIf;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillManagerTest {
    private final SkillManager skillManager = new SkillManager();

    @Test
    void movementWithoutModifierIsUnchanged() {
        assertEquals(10, skillManager.applyMovement(new EconomyHero(), 10));
    }

    @Test
    void applyMovementIgnoresSkillsWithoutMovementModifier() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());

        assertEquals(10, skillManager.applyMovement(hero, 10));
    }

    @Test
    void logisticsChangesMovementByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LogisticsSkill());
        assertEquals(11, skillManager.applyMovement(hero, 10));

        hero.upgradeSkill(new LogisticsSkill());
        assertEquals(12, skillManager.applyMovement(hero, 10));

        hero.upgradeSkill(new LogisticsSkill());
        assertEquals(13, skillManager.applyMovement(hero, 10));
    }

    @Test
    void damageWithoutModifierIsUnchanged() {
        assertEquals(100, skillManager.applyDamage(new EconomyHero(), 100));
    }

    @Test
    void applyDamageIgnoresSkillsWithoutDamageModifier() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LogisticsSkill());

        assertEquals(100, skillManager.applyDamage(hero, 100));
    }

    @Test
    void offenceChangesDamageByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());
        assertEquals(110, skillManager.applyDamage(hero, 100));

        hero.upgradeSkill(new OffenceSkill());
        assertEquals(120, skillManager.applyDamage(hero, 100));

        hero.upgradeSkill(new OffenceSkill());
        assertEquals(130, skillManager.applyDamage(hero, 100));
    }

    @Test
    void repeatedDamageCalculationsDoNotAccumulate() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());

        assertEquals(110, skillManager.applyDamage(hero, 100));
        assertEquals(110, skillManager.applyDamage(hero, 100));
    }

    @Test
    void spellPowerWithoutMatchingSchoolIsUnchanged() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new AirMagicSkill());

        assertEquals(100, skillManager.applySpellPower(hero, SkillName.FIRE_MAGIC, 100));
    }

    @Test
    void matchingMagicSchoolChangesSpellPower() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new AirMagicSkill());

        assertEquals(110, skillManager.applySpellPower(hero, SkillName.AIR_MAGIC, 100));
    }

    @Test
    void earthMagicChangesOnlyEarthSpellPower() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new EarthMagicSkill());

        assertEquals(110, skillManager.applySpellPower(hero, SkillName.EARTH_MAGIC, 100));
        assertEquals(100, skillManager.applySpellPower(hero, SkillName.AIR_MAGIC, 100));
    }

    @Test
    void fireMagicChangesOnlyFireSpellPower() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new FireMagicSkill());

        assertEquals(110, skillManager.applySpellPower(hero, SkillName.FIRE_MAGIC, 100));
        assertEquals(100, skillManager.applySpellPower(hero, SkillName.WATER_MAGIC, 100));
    }

    @Test
    void waterMagicChangesOnlyWaterSpellPower() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new WaterMagicSkill());

        assertEquals(110, skillManager.applySpellPower(hero, SkillName.WATER_MAGIC, 100));
        assertEquals(100, skillManager.applySpellPower(hero, SkillName.FIRE_MAGIC, 100));
    }

    @Test
    void repeatedSpellPowerCalculationsDoNotAccumulate() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new AirMagicSkill());

        assertEquals(110, skillManager.applySpellPower(hero, SkillName.AIR_MAGIC, 100));
        assertEquals(110, skillManager.applySpellPower(hero, SkillName.AIR_MAGIC, 100));
    }

    @Test
    void moraleWithoutModifierIsUnchanged() {
        assertEquals(0, skillManager.applyMorale(new EconomyHero(), 0));
    }

    @Test
    void leadershipChangesMoraleByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LeadershipSkill());
        assertEquals(1, skillManager.applyMorale(hero, 0));

        hero.upgradeSkill(new LeadershipSkill());
        assertEquals(2, skillManager.applyMorale(hero, 0));

        hero.upgradeSkill(new LeadershipSkill());
        assertEquals(3, skillManager.applyMorale(hero, 0));
    }

    @Test
    void multipleMoraleModifiersAreAppliedInOrder() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new TestMoraleSkill(SkillName.NECROMANCY, 1));
        hero.upgradeSkill(new TestMoraleSkill(SkillName.SORCERY, 2));

        assertEquals(8, skillManager.applyMorale(hero, 5));
    }

    @Test
    void luckWithoutModifierIsUnchanged() {
        assertEquals(0, skillManager.applyLuck(new EconomyHero(), 0));
    }

    @Test
    void applyLuckIgnoresSkillsWithoutLuckModifier() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LeadershipSkill());

        assertEquals(0, skillManager.applyLuck(hero, 0));
    }

    @Test
    void luckChangesLuckByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LuckSkill());
        assertEquals(1, skillManager.applyLuck(hero, 0));

        hero.upgradeSkill(new LuckSkill());
        assertEquals(2, skillManager.applyLuck(hero, 0));

        hero.upgradeSkill(new LuckSkill());
        assertEquals(3, skillManager.applyLuck(hero, 0));
    }

    @Test
    void tacticsRangeWithoutModifierIsUnchanged() {
        assertEquals(0, skillManager.applyTacticsRange(new EconomyHero(), 0));
    }

    @Test
    void applyTacticsRangeIgnoresSkillsWithoutTacticsModifier() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LuckSkill());

        assertEquals(0, skillManager.applyTacticsRange(hero, 0));
    }

    @Test
    void tacticsChangesRangeByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new TacticsSkill());
        assertEquals(1, skillManager.applyTacticsRange(hero, 0));

        hero.upgradeSkill(new TacticsSkill());
        assertEquals(2, skillManager.applyTacticsRange(hero, 0));

        hero.upgradeSkill(new TacticsSkill());
        assertEquals(3, skillManager.applyTacticsRange(hero, 0));
    }

    private static class TestMoraleSkill extends AbstractSkill implements MoraleModifierIf {
        private final SkillName name;
        private final int moraleBonus;

        private TestMoraleSkill(SkillName name, int moraleBonus) {
            this.name = name;
            this.moraleBonus = moraleBonus;
        }

        @Override
        public SkillName getName() {
            return name;
        }

        @Override
        public void upgrade() {
            throw new IllegalStateException("Test skill does not support upgrade.");
        }

        @Override
        public float getFactor() {
            return moraleBonus;
        }

        @Override
        public int changeMorale(int currentMorale) {
            return currentMorale + moraleBonus;
        }
    }
}
