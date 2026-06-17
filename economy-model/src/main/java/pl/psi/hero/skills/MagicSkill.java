package pl.psi.hero.skills;

public class MagicSkill extends AbstractSkill {
    private final SkillName name;
    private float levelFactor; // 1.0f (Basic), 2.0f (Advanced), 3.0f (Expert)

    public MagicSkill(SkillName name) {
        super();
        if (!name.isMagic()) {
            throw new IllegalArgumentException("MagicSkill name must be a magic school.");
        }
        this.name = name;
        this.levelFactor = 1.0f;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
            this.levelFactor = 2.0f;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
            this.levelFactor = 3.0f;
        } else {
            throw new IllegalStateException("Cannot upgrade from Expert level.");
        }
    }

    @Override
    public SkillName getName() {
        return name;
    }

    @Override
    public float getFactor() {
        return levelFactor;
    }
}
