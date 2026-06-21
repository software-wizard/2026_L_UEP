package pl.psi.hero.skills;

public class NamedSkill extends AbstractSkill {
    private final SkillName name;

    public NamedSkill(SkillName name, SkillLevel level) {
        this.name = name;
        setLevel(level);
    }

    @Override
    public SkillName getName() {
        return name;
    }

    @Override
    public void upgrade() {
        if (this.level.equals(SkillLevel.BASIC)) {
            this.level = SkillLevel.ADVANCED;
        } else if (this.level.equals(SkillLevel.ADVANCED)) {
            this.level = SkillLevel.EXPERT;
        }
    }

    @Override
    public float getFactor() {
        return 0;
    }
}
