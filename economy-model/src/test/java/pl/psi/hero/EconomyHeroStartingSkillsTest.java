package pl.psi.hero;

import org.junit.jupiter.api.Test;
import pl.psi.hero.skills.HeroStartingSkill;
import pl.psi.hero.skills.NamedSkill;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillName;
import pl.psi.hero.skills.impl.OffenceSkill;
import pl.psi.map.resources.Resources;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EconomyHeroStartingSkillsTest {

    @Test
    void heroClassFromConstructorIsPreserved() {
        EconomyHero hero = heroWithStartingSkills(List.of());

        assertEquals(HeroClass.DEATH_KNIGHT, hero.getHeroClass());
    }

    @Test
    void startingSkillsAreAddedToHeroSkills() {
        EconomyHero hero = heroWithStartingSkills(List.of(
                new HeroStartingSkill(SkillName.NECROMANCY, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.OFFENCE, SkillLevel.BASIC)
        ));

        assertEquals(2, hero.getSkills().size());
        assertEquals(SkillLevel.BASIC, getSkillLevel(hero, SkillName.NECROMANCY));
        assertEquals(SkillLevel.BASIC, getSkillLevel(hero, SkillName.OFFENCE));
    }

    @Test
    void existingStartingSkillIsUpgradedInsteadOfDuplicated() {
        EconomyHero hero = heroWithStartingSkills(List.of(
                new HeroStartingSkill(SkillName.OFFENCE, SkillLevel.BASIC)
        ));

        hero.upgradeSkill(new OffenceSkill());

        assertEquals(1, hero.getSkills().size());
        assertEquals(SkillLevel.ADVANCED, getSkillLevel(hero, SkillName.OFFENCE));
    }

    @Test
    void namedStartingSkillCanBeUpgradedLater() {
        EconomyHero hero = heroWithStartingSkills(List.of(
                new HeroStartingSkill(SkillName.NECROMANCY, SkillLevel.BASIC)
        ));

        hero.upgradeSkill(new NamedSkill(SkillName.NECROMANCY, SkillLevel.BASIC));

        assertEquals(1, hero.getSkills().size());
        assertEquals(SkillLevel.ADVANCED, getSkillLevel(hero, SkillName.NECROMANCY));
    }

    @Test
    void namedStartingSkillCanUpgradeFromAdvancedToExpert() {
        EconomyHero hero = heroWithStartingSkills(List.of(
                new HeroStartingSkill(SkillName.NECROMANCY, SkillLevel.BASIC)
        ));

        hero.upgradeSkill(new NamedSkill(SkillName.NECROMANCY, SkillLevel.BASIC));
        hero.upgradeSkill(new NamedSkill(SkillName.NECROMANCY, SkillLevel.BASIC));

        assertEquals(1, hero.getSkills().size());
        assertEquals(SkillLevel.EXPERT, getSkillLevel(hero, SkillName.NECROMANCY));
    }

    @Test
    void addingExpertNamedSkillAgainDoesNotCreateDuplicate() {
        EconomyHero hero = heroWithStartingSkills(List.of(
                new HeroStartingSkill(SkillName.NECROMANCY, SkillLevel.EXPERT)
        ));

        hero.upgradeSkill(new NamedSkill(SkillName.NECROMANCY, SkillLevel.BASIC));

        assertEquals(1, hero.getSkills().size());
        assertEquals(SkillLevel.EXPERT, getSkillLevel(hero, SkillName.NECROMANCY));
    }

    @Test
    void startingSkillsCountTowardEightSkillLimit() {
        EconomyHero hero = heroWithStartingSkills(eightStartingSkills());

        assertEquals(8, hero.getSkills().size());
        assertThrows(IllegalStateException.class, () -> hero.upgradeSkill(new NamedSkill(SkillName.LUCK, SkillLevel.BASIC)));
    }

    @Test
    void ninthNewStartingSkillIsRejected() {
        assertThrows(IllegalStateException.class, () -> heroWithStartingSkills(List.of(
                new HeroStartingSkill(SkillName.NECROMANCY, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.OFFENCE, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.ARMORER, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.LEARNING, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.LOGISTICS, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.PATHFINDING, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.TACTICS, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.LEADERSHIP, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.LUCK, SkillLevel.BASIC)
        )));
    }

    private EconomyHero heroWithStartingSkills(List<HeroStartingSkill> startingSkills) {
        return new EconomyHero(
                EconomyHero.Fraction.NECROPOLIS,
                HeroClass.DEATH_KNIGHT,
                new Resources(3000, 0, 0, 0, 0, 0, 0),
                new Statistics(2, 1, 1, 1),
                startingSkills
        );
    }

    private List<HeroStartingSkill> eightStartingSkills() {
        return List.of(
                new HeroStartingSkill(SkillName.NECROMANCY, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.OFFENCE, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.ARMORER, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.LEARNING, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.LOGISTICS, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.PATHFINDING, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.TACTICS, SkillLevel.BASIC),
                new HeroStartingSkill(SkillName.LEADERSHIP, SkillLevel.BASIC)
        );
    }

    private SkillLevel getSkillLevel(EconomyHero hero, SkillName skillName) {
        return hero.getSkills().stream()
                .filter(skill -> skill.getName().equals(skillName))
                .findFirst()
                .orElseThrow()
                .getLevel();
    }
}
