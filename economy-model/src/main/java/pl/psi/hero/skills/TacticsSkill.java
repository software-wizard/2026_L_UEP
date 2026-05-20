package pl.psi.hero.skills;

public class TacticsSkill extends AbstractSkill {
    private float tacticsFactor;

    public TacticsSkill() {
        super();
        this.tacticsFactor = 1.0f;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
            this.tacticsFactor = 2.0f;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
            this.tacticsFactor = 3.0f;
        } else {
            throw new IllegalStateException("Cannot upgrade from Expert level.");
        }
    }

    @Override
    public SkillName getName() {
        return SkillName.TACTICS;
    }

    @Override
    public float getFactor() {
        return tacticsFactor;
    }
}
