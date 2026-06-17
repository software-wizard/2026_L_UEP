package pl.psi.hero.skills.impl;

import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;

public class PathfindingSkill extends AbstractSkill {
    private float terrainPenaltyReduction;

    public PathfindingSkill() {
        super();
        terrainPenaltyReduction = 0.25f;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
            terrainPenaltyReduction = 0.50f;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
            terrainPenaltyReduction = 0.75f;
        } else {
            throw new IllegalStateException("Cannot upgrade from Expert level.");
        }
    }

    @Override
    public SkillName getName() {
        return SkillName.PATHFINDING;
    }

    @Override
    public float getFactor() {
        return terrainPenaltyReduction;
    }
}
