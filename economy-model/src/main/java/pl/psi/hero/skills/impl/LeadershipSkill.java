package pl.psi.hero.skills.impl;

import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;
import pl.psi.hero.skills.modifiers.MoraleModifierIf;

public class LeadershipSkill extends AbstractSkill implements MoraleModifierIf {
    private float moraleBonus;

    public LeadershipSkill() {
        super();
        moraleBonus = 1.0f;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
            moraleBonus = 2.0f;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
            moraleBonus = 3.0f;
        } else {
            throw new IllegalStateException("Cannot upgrade from Expert level.");
        }
    }

    @Override
    public SkillName getName() {
        return SkillName.LEADERSHIP;
    }

    @Override
    public float getFactor() {
        return moraleBonus;
    }

    @Override
    public int changeMorale(int currentMorale) {
        return currentMorale + Math.round(moraleBonus);
    }
}
