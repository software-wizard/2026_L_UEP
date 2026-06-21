package pl.psi.hero.skills.impl;

import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;
import pl.psi.hero.skills.modifiers.TerrainPenaltyModifierIf;

public class PathfindingSkill extends AbstractSkill implements TerrainPenaltyModifierIf {
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

    @Override
    public int changeTerrainPenalty(int currentPenalty) {
        if (currentPenalty <= 0) {
            return 0;
        }
        return Math.max(0, Math.round(currentPenalty * (1 - terrainPenaltyReduction)));
    }
}
