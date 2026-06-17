package pl.psi.hero.skills.impl;

import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;
import pl.psi.hero.skills.modifiers.ExpModifierIf;

public class LearningSkill extends AbstractSkill implements ExpModifierIf {
    private float expBonus;

    public LearningSkill() {
        super();
        this.expBonus = 0.05f;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
            this.expBonus = 0.10f;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
            this.expBonus = 0.15f;
        } else {
            throw new IllegalStateException("Cannot upgrade from Expert level.");
        }
    }

    @Override
    public SkillName getName() {
        return SkillName.LEARNING;
    }

    @Override
    public float getFactor() {
        return expBonus;
    }

    @Override
    public double getExpMultiplier() {
        return 1.0 + expBonus;
    }
}
