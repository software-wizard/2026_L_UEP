package pl.psi.hero.skills;

import pl.psi.hero.EconomyHero;
import pl.psi.hero.skills.modifiers.DamageModifierIf;
import pl.psi.hero.skills.modifiers.LuckModifierIf;
import pl.psi.hero.skills.modifiers.MoraleModifierIf;
import pl.psi.hero.skills.modifiers.MovementModifierIf;
import pl.psi.hero.skills.modifiers.SpellModifierIf;
import pl.psi.hero.skills.modifiers.TacticsModifierIf;

public class SkillManager {
    public int applyMovement(EconomyHero hero, int baseMove) {
        int currentMove = baseMove;
        for (AbstractSkill skill : hero.getSkills()) {
            if (skill instanceof MovementModifierIf) {
                currentMove = ((MovementModifierIf) skill).changeMove(currentMove);
            }
        }
        return currentMove;
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
