package pl.psi.hero.skills.impl;

import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;
import pl.psi.hero.skills.modifiers.MovementModifierIf;

public class LogisticsSkill extends AbstractSkill implements MovementModifierIf {
    private float movementBonusFactor;

    public LogisticsSkill() {
        super();
        this.movementBonusFactor = 0.1f;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
            this.movementBonusFactor = 0.2f;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
            this.movementBonusFactor = 0.3f;
        } else {
            throw new IllegalStateException("Cannot upgrade from Expert level.");
        }
    }

    @Override
    public SkillName getName() {
        return SkillName.LOGISTICS;
    }

    @Override
    public float getFactor() {
        return movementBonusFactor;
    }

    @Override
    public int changeMove(int currentMove) {
        return Math.round(currentMove * (1 + movementBonusFactor));
    }
}
