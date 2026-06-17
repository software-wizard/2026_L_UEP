package pl.psi.hero.skills;

import pl.psi.hero.skills.impl.AirMagicSkill;
import pl.psi.hero.skills.impl.ArmorerSkill;
import pl.psi.hero.skills.impl.EarthMagicSkill;
import pl.psi.hero.skills.impl.FireMagicSkill;
import pl.psi.hero.skills.impl.LearningSkill;
import pl.psi.hero.skills.impl.LeadershipSkill;
import pl.psi.hero.skills.impl.LogisticsSkill;
import pl.psi.hero.skills.impl.LuckSkill;
import pl.psi.hero.skills.impl.OffenceSkill;
import pl.psi.hero.skills.impl.PathfindingSkill;
import pl.psi.hero.skills.impl.TacticsSkill;
import pl.psi.hero.skills.impl.WaterMagicSkill;

public class SkillFactory {

    public AbstractSkill create(SkillName skillName) {
        return create(skillName, SkillLevel.BASIC);
    }

    public AbstractSkill create(SkillName skillName, SkillLevel level) {
        AbstractSkill skill;
        switch (skillName) {
            case OFFENCE:
                skill = new OffenceSkill();
                break;
            case ARMORER:
                skill = new ArmorerSkill();
                break;
            case LEARNING:
                skill = new LearningSkill();
                break;
            case LOGISTICS:
                skill = new LogisticsSkill();
                break;
            case PATHFINDING:
                skill = new PathfindingSkill();
                break;
            case TACTICS:
                skill = new TacticsSkill();
                break;
            case LEADERSHIP:
                skill = new LeadershipSkill();
                break;
            case LUCK:
                skill = new LuckSkill();
                break;
            case AIR_MAGIC:
                skill = new AirMagicSkill();
                break;
            case EARTH_MAGIC:
                skill = new EarthMagicSkill();
                break;
            case FIRE_MAGIC:
                skill = new FireMagicSkill();
                break;
            case WATER_MAGIC:
                skill = new WaterMagicSkill();
                break;
            default:
                skill = new NamedSkill(skillName, level);
                break;
        }
        skill.setLevel(level);
        return skill;
    }
}
