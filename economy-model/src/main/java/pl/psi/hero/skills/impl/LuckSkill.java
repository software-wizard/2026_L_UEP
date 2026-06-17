package pl.psi.hero.skills.impl;

import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;
import pl.psi.hero.skills.modifiers.LuckModifierIf;

public class LuckSkill extends AbstractSkill implements LuckModifierIf {
    private float luckBonus;

    public LuckSkill() {
        super();
        luckBonus = 1.0f;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
            luckBonus = 2.0f;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
            luckBonus = 3.0f;
        } else {
            throw new IllegalStateException("Cannot upgrade from Expert level.");
        }
    }

    @Override
    public SkillName getName() {
        return SkillName.LUCK;
    }

    @Override
    public float getFactor() {
        return luckBonus;
    }

    @Override
    public int changeLuck(int currentLuck) {
        return currentLuck + Math.round(luckBonus);
    }
}
