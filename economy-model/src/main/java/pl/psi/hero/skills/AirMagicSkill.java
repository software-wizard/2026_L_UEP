package pl.psi.hero.skills;

public class AirMagicSkill extends AbstractSkill {
    private float magicFactor;

    public AirMagicSkill() {
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
        return SkillName.AIR_MAGIC;
    }

    @Override
    public float getFactor() {
        return magicFactor;
    }
}
