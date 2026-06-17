package pl.psi.hero.skills;

public class HeroStartingSkill {
    private final SkillName name;
    private final SkillLevel level;

    public HeroStartingSkill(SkillName name, SkillLevel level) {
        this.name = name;
        this.level = level;
    }

    public SkillName getName() {
        return name;
    }

    public SkillLevel getLevel() {
        return level;
    }
}
