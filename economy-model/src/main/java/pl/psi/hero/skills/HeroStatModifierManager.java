package pl.psi.hero.skills;

import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.modifiers.DamageModifierIf;
import pl.psi.hero.skills.modifiers.DamageReductionModifierIf;
import pl.psi.hero.skills.modifiers.ExpModifierIf;
import pl.psi.hero.skills.modifiers.LuckModifierIf;
import pl.psi.hero.skills.modifiers.MoraleModifierIf;
import pl.psi.hero.skills.modifiers.MovementModifierIf;
import pl.psi.hero.skills.modifiers.SpellModifierIf;
import pl.psi.hero.skills.modifiers.TacticsModifierIf;
import pl.psi.hero.skills.modifiers.TerrainPenaltyModifierIf;

public class HeroStatModifierManager {
    public int applyExperience(EconomyHero hero, int baseExperience) {
        double totalMultiplier = 1.0;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof ExpModifierIf) {
                totalMultiplier *= ((ExpModifierIf) skill).getExpMultiplier();
            }
        }
        return (int) Math.round(baseExperience * totalMultiplier);
    }

    public int applyMovement(EconomyHero hero, int baseMove) {
        int currentMove = baseMove;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof MovementModifierIf) {
                currentMove = ((MovementModifierIf) skill).changeMove(currentMove);
            }
        }
        return currentMove;
    }

    public int applyTerrainPenalty(EconomyHero hero, int basePenalty) {
        int currentPenalty = Math.max(0, basePenalty);
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof TerrainPenaltyModifierIf) {
                currentPenalty = ((TerrainPenaltyModifierIf) skill).changeTerrainPenalty(currentPenalty);
            }
        }
        return Math.max(0, currentPenalty);
    }

    public int applyDamage(EconomyHero hero, int baseDamage) {
        int currentDamage = baseDamage;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof DamageModifierIf) {
                currentDamage = ((DamageModifierIf) skill).changeDamage(currentDamage);
            }
        }
        return currentDamage;
    }

    public float getDamageBonusFactor(EconomyHero hero) {
        float damageMultiplier = 1.0f;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof DamageModifierIf) {
                damageMultiplier *= 1.0f + ((DamageModifierIf) skill).getDamageBonusFactor();
            }
        }
        return damageMultiplier - 1.0f;
    }

    public float getDamageReductionFactor(EconomyHero hero) {
        float remainingDamageFactor = 1.0f;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof DamageReductionModifierIf) {
                remainingDamageFactor *= 1.0f
                        - ((DamageReductionModifierIf) skill).getDamageReductionFactor();
            }
        }
        return 1.0f - remainingDamageFactor;
    }

    public int applySpellPower(EconomyHero hero, SkillName spellSchool, int baseSpellPower) {
        int currentSpellPower = baseSpellPower;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof SpellModifierIf) {
                currentSpellPower = ((SpellModifierIf) skill).changeSpellPower(spellSchool, currentSpellPower);
            }
        }
        return currentSpellPower;
    }

    public int applyMorale(EconomyHero hero, int baseMorale) {
        int currentMorale = baseMorale;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof MoraleModifierIf) {
                currentMorale = ((MoraleModifierIf) skill).changeMorale(currentMorale);
            }
        }
        return currentMorale;
    }

    public int applyLuck(EconomyHero hero, int baseLuck) {
        int currentLuck = baseLuck;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof LuckModifierIf) {
                currentLuck = ((LuckModifierIf) skill).changeLuck(currentLuck);
            }
        }
        return currentLuck;
    }

    public int applyTacticsRange(EconomyHero hero, int baseRange) {
        int currentRange = baseRange;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof TacticsModifierIf) {
                currentRange = ((TacticsModifierIf) skill).changeTacticsRange(currentRange);
            }
        }
        return currentRange;
    }
}
