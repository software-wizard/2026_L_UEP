package pl.psi.hero.skills;

public class LogisticsSkill extends AbstractSkill {
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
}
