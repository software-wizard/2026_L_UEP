package pl.psi.hero.skills;

import java.util.List;
import java.util.stream.Collectors;

public class SkillRegistry {
    private static final List<SkillName> AVAILABLE_SKILLS = List.of(
            SkillName.OFFENCE,
            SkillName.ARMORER,
            SkillName.LEARNING,
            SkillName.LOGISTICS,
            SkillName.PATHFINDING,
            SkillName.TACTICS,
            SkillName.LEADERSHIP,
            SkillName.LUCK,
            SkillName.AIR_MAGIC,
            SkillName.EARTH_MAGIC,
            SkillName.FIRE_MAGIC,
            SkillName.WATER_MAGIC
    );

    private final List<SkillName> availableSkills;
    private final SkillFactory skillFactory;

    public SkillRegistry() {
        this(new SkillFactory());
    }

    public SkillRegistry(SkillFactory skillFactory) {
        this(AVAILABLE_SKILLS, skillFactory);
    }

    public SkillRegistry(List<SkillName> availableSkills, SkillFactory skillFactory) {
        this.availableSkills = availableSkills;
        this.skillFactory = skillFactory;
    }

    public List<SkillName> getAvailableSkillNames() {
        return availableSkills;
    }

    public List<AbstractSkill> getAllSkillTemplates() {
        return availableSkills.stream()
                .map(skillFactory::create)
                .collect(Collectors.toList());
    }
}
