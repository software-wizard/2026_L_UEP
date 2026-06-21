package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.impl.EarthMagicSkill;
import pl.psi.hero.skills.impl.AirMagicSkill;
import pl.psi.hero.skills.impl.ArmorerSkill;
import pl.psi.hero.skills.impl.FireMagicSkill;
import pl.psi.hero.skills.impl.LeadershipSkill;
import pl.psi.hero.skills.impl.LogisticsSkill;
import pl.psi.hero.skills.impl.LuckSkill;
import pl.psi.hero.skills.impl.OffenceSkill;
import pl.psi.hero.skills.impl.PathfindingSkill;
import pl.psi.hero.skills.impl.TacticsSkill;
import pl.psi.hero.skills.impl.WaterMagicSkill;
import pl.psi.hero.skills.modifiers.ExpModifierIf;
import pl.psi.hero.skills.modifiers.MoraleModifierIf;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HeroStatModifierManagerTest {
    private final HeroStatModifierManager heroStatModifierManager = new HeroStatModifierManager();

    @Test
    void experienceWithoutExpModifierIsUnchanged() {
        assertEquals(100, heroStatModifierManager.applyExperience(new EconomyHero(), 100));
    }

    @Test
    void multipleExpModifiersAreMultipliedBeforeFinalRounding() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new TestExpSkill(SkillName.NECROMANCY, 1.05));
        hero.upgradeSkill(new TestExpSkill(SkillName.SORCERY, 1.10));

        assertEquals(116, heroStatModifierManager.applyExperience(hero, 100));
    }

    @Test
    void skillWithoutExpModifierDoesNotChangeExperience() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());

        assertEquals(100, heroStatModifierManager.applyExperience(hero, 100));
    }

    @Test
    void movementWithoutModifierIsUnchanged() {
        assertEquals(1000, heroStatModifierManager.applyMovement(new EconomyHero(), 1000));
    }

    @Test
    void applyMovementIgnoresSkillsWithoutMovementModifier() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());

        assertEquals(1000, heroStatModifierManager.applyMovement(hero, 1000));
    }

    @Test
    void logisticsChangesMovementByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LogisticsSkill());
        assertEquals(1100, heroStatModifierManager.applyMovement(hero, 1000));

        hero.upgradeSkill(new LogisticsSkill());
        assertEquals(1200, heroStatModifierManager.applyMovement(hero, 1000));

        hero.upgradeSkill(new LogisticsSkill());
        assertEquals(1300, heroStatModifierManager.applyMovement(hero, 1000));
    }

    @Test
    void terrainPenaltyWithoutModifierIsUnchanged() {
        assertEquals(20, heroStatModifierManager.applyTerrainPenalty(new EconomyHero(), 20));
    }

    @Test
    void pathfindingReducesTerrainPenaltyByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new PathfindingSkill());
        assertEquals(15, heroStatModifierManager.applyTerrainPenalty(hero, 20));

        hero.upgradeSkill(new PathfindingSkill());
        assertEquals(10, heroStatModifierManager.applyTerrainPenalty(hero, 20));

        hero.upgradeSkill(new PathfindingSkill());
        assertEquals(5, heroStatModifierManager.applyTerrainPenalty(hero, 20));
    }

    @Test
    void pathfindingKeepsZeroTerrainPenaltyAtZero() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new PathfindingSkill());

        assertEquals(0, heroStatModifierManager.applyTerrainPenalty(hero, 0));
    }

    @Test
    void terrainPenaltyCannotBecomeNegative() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new PathfindingSkill());

        assertEquals(0, heroStatModifierManager.applyTerrainPenalty(hero, -20));
    }

    @Test
    void effectiveMoveRangeUsesLogisticsWithoutChangingBaseMoveRange() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LogisticsSkill());

        assertEquals(1100, hero.getEffectiveMoveRange());
        assertEquals(1000, hero.getMoveRange());
    }

    @Test
    void resetMoveRangeUsesEffectiveMoveRange() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LogisticsSkill());
        hero.deductMove(200);

        hero.resetMoveRange();

        assertEquals(1100, hero.getRemainingMoveRange());
    }

    @Test
    void repeatedMoveRangeResetsDoNotAccumulateLogisticsBonus() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LogisticsSkill());

        hero.resetMoveRange();
        assertEquals(1100, hero.getRemainingMoveRange());

        hero.resetMoveRange();
        assertEquals(1100, hero.getRemainingMoveRange());
        assertEquals(1000, hero.getMoveRange());
    }

    @Test
    void damageWithoutModifierIsUnchanged() {
        assertEquals(100, heroStatModifierManager.applyDamage(new EconomyHero(), 100));
    }

    @Test
    void applyDamageIgnoresSkillsWithoutDamageModifier() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LogisticsSkill());

        assertEquals(100, heroStatModifierManager.applyDamage(hero, 100));
    }

    @Test
    void offenceChangesDamageByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());
        assertEquals(110, heroStatModifierManager.applyDamage(hero, 100));

        hero.upgradeSkill(new OffenceSkill());
        assertEquals(120, heroStatModifierManager.applyDamage(hero, 100));

        hero.upgradeSkill(new OffenceSkill());
        assertEquals(130, heroStatModifierManager.applyDamage(hero, 100));
    }

    @Test
    void repeatedDamageCalculationsDoNotAccumulate() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());

        assertEquals(110, heroStatModifierManager.applyDamage(hero, 100));
        assertEquals(110, heroStatModifierManager.applyDamage(hero, 100));
    }

    @Test
    void heroWithoutCombatSkillsHasZeroDamageFactors() {
        EconomyHero hero = new EconomyHero();

        assertEquals(0.0f, heroStatModifierManager.getDamageBonusFactor(hero), 0.0001f);
        assertEquals(0.0f, heroStatModifierManager.getDamageReductionFactor(hero), 0.0001f);
    }

    @Test
    void offenceDamageBonusFactorMatchesSkillLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());
        assertEquals(0.1f, heroStatModifierManager.getDamageBonusFactor(hero), 0.0001f);

        hero.upgradeSkill(new OffenceSkill());
        assertEquals(0.2f, heroStatModifierManager.getDamageBonusFactor(hero), 0.0001f);

        hero.upgradeSkill(new OffenceSkill());
        assertEquals(0.3f, heroStatModifierManager.getDamageBonusFactor(hero), 0.0001f);
    }

    @Test
    void armorerDamageReductionFactorMatchesSkillLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new ArmorerSkill());
        assertEquals(0.1f, heroStatModifierManager.getDamageReductionFactor(hero), 0.0001f);

        hero.upgradeSkill(new ArmorerSkill());
        assertEquals(0.2f, heroStatModifierManager.getDamageReductionFactor(hero), 0.0001f);

        hero.upgradeSkill(new ArmorerSkill());
        assertEquals(0.3f, heroStatModifierManager.getDamageReductionFactor(hero), 0.0001f);
    }

    @Test
    void spellPowerWithoutMatchingSchoolIsUnchanged() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new AirMagicSkill());

        assertEquals(100, heroStatModifierManager.applySpellPower(hero, SkillName.FIRE_MAGIC, 100));
    }

    @Test
    void matchingMagicSchoolChangesSpellPower() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new AirMagicSkill());

        assertEquals(110, heroStatModifierManager.applySpellPower(hero, SkillName.AIR_MAGIC, 100));
    }

    @Test
    void earthMagicChangesOnlyEarthSpellPower() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new EarthMagicSkill());

        assertEquals(110, heroStatModifierManager.applySpellPower(hero, SkillName.EARTH_MAGIC, 100));
        assertEquals(100, heroStatModifierManager.applySpellPower(hero, SkillName.AIR_MAGIC, 100));
    }

    @Test
    void fireMagicChangesOnlyFireSpellPower() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new FireMagicSkill());

        assertEquals(110, heroStatModifierManager.applySpellPower(hero, SkillName.FIRE_MAGIC, 100));
        assertEquals(100, heroStatModifierManager.applySpellPower(hero, SkillName.WATER_MAGIC, 100));
    }

    @Test
    void waterMagicChangesOnlyWaterSpellPower() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new WaterMagicSkill());

        assertEquals(110, heroStatModifierManager.applySpellPower(hero, SkillName.WATER_MAGIC, 100));
        assertEquals(100, heroStatModifierManager.applySpellPower(hero, SkillName.FIRE_MAGIC, 100));
    }

    @Test
    void repeatedSpellPowerCalculationsDoNotAccumulate() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new AirMagicSkill());

        assertEquals(110, heroStatModifierManager.applySpellPower(hero, SkillName.AIR_MAGIC, 100));
        assertEquals(110, heroStatModifierManager.applySpellPower(hero, SkillName.AIR_MAGIC, 100));
    }

    @Test
    void moraleWithoutModifierIsUnchanged() {
        assertEquals(0, heroStatModifierManager.applyMorale(new EconomyHero(), 0));
    }

    @Test
    void leadershipChangesMoraleByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LeadershipSkill());
        assertEquals(1, heroStatModifierManager.applyMorale(hero, 0));

        hero.upgradeSkill(new LeadershipSkill());
        assertEquals(2, heroStatModifierManager.applyMorale(hero, 0));

        hero.upgradeSkill(new LeadershipSkill());
        assertEquals(3, heroStatModifierManager.applyMorale(hero, 0));
    }

    @Test
    void multipleMoraleModifiersAreAppliedInOrder() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new TestMoraleSkill(SkillName.NECROMANCY, 1));
        hero.upgradeSkill(new TestMoraleSkill(SkillName.SORCERY, 2));

        assertEquals(8, heroStatModifierManager.applyMorale(hero, 5));
    }

    @Test
    void luckWithoutModifierIsUnchanged() {
        assertEquals(0, heroStatModifierManager.applyLuck(new EconomyHero(), 0));
    }

    @Test
    void applyLuckIgnoresSkillsWithoutLuckModifier() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LeadershipSkill());

        assertEquals(0, heroStatModifierManager.applyLuck(hero, 0));
    }

    @Test
    void luckChangesLuckByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LuckSkill());
        assertEquals(1, heroStatModifierManager.applyLuck(hero, 0));

        hero.upgradeSkill(new LuckSkill());
        assertEquals(2, heroStatModifierManager.applyLuck(hero, 0));

        hero.upgradeSkill(new LuckSkill());
        assertEquals(3, heroStatModifierManager.applyLuck(hero, 0));
    }

    @Test
    void tacticsRangeWithoutModifierIsUnchanged() {
        assertEquals(0, heroStatModifierManager.applyTacticsRange(new EconomyHero(), 0));
    }

    @Test
    void applyTacticsRangeIgnoresSkillsWithoutTacticsModifier() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new LuckSkill());

        assertEquals(0, heroStatModifierManager.applyTacticsRange(hero, 0));
    }

    @Test
    void tacticsChangesRangeByLevel() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new TacticsSkill());
        assertEquals(1, heroStatModifierManager.applyTacticsRange(hero, 0));

        hero.upgradeSkill(new TacticsSkill());
        assertEquals(2, heroStatModifierManager.applyTacticsRange(hero, 0));

        hero.upgradeSkill(new TacticsSkill());
        assertEquals(3, heroStatModifierManager.applyTacticsRange(hero, 0));
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

    private static class TestExpSkill extends AbstractSkill implements ExpModifierIf {
        private final SkillName name;
        private final double multiplier;

        private TestExpSkill(SkillName name, double multiplier) {
            this.name = name;
            this.multiplier = multiplier;
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
            return (float) multiplier;
        }

        @Override
        public double getExpMultiplier() {
            return multiplier;
        }
    }
}
