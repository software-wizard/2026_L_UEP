package pl.psi.hero.skills.impl;

import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;
import pl.psi.hero.skills.modifiers.SpellModifierIf;

public class EarthMagicSkill extends AbstractSkill implements SpellModifierIf {
    private float magicFactor;

    public EarthMagicSkill() {
        super();
        this.magicFactor = 0.1f;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
            this.magicFactor = 0.2f;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
            this.magicFactor = 0.3f;
        } else {
            throw new IllegalStateException("Cannot upgrade from Expert level.");
        }
    }

    @Override
    public SkillName getName() {
        return SkillName.EARTH_MAGIC;
    }

    @Override
    public float getFactor() {
        return magicFactor;
    }

    @Override
    public int changeSpellPower(SkillName spellSchool, int currentSpellPower) {
        return spellSchool == getName() ? Math.round(currentSpellPower * (1 + magicFactor)) : currentSpellPower;
    }
}
