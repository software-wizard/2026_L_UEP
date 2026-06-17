package pl.psi.hero;

import org.junit.jupiter.api.Test;
import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.SkillName;
import pl.psi.hero.skills.impl.AirMagicSkill;
import pl.psi.hero.skills.impl.ArmorerSkill;
import pl.psi.hero.skills.impl.EarthMagicSkill;
import pl.psi.hero.skills.impl.FireMagicSkill;
import pl.psi.hero.skills.impl.LearningSkill;
import pl.psi.hero.skills.impl.LogisticsSkill;
import pl.psi.hero.skills.impl.OffenceSkill;
import pl.psi.hero.skills.impl.TacticsSkill;
import pl.psi.hero.skills.impl.WaterMagicSkill;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EconomyHeroSkillChoiceTest {

    @Test
    void shouldLimitHeroToEightSkills() {
        EconomyHero hero = new EconomyHero();

        learnEightSkills(hero);

        assertEquals(8, hero.getSkills().size());
        assertThrows(IllegalStateException.class, () -> hero.upgradeSkill(new WaterMagicSkill()));
    }

    @Test
    void shouldOfferOneNewSkillAndOneUpgradeableSkillWhenBothAreAvailable() {
        EconomyHero hero = new EconomyHero();
        hero.upgradeSkill(new OffenceSkill());

        List<AbstractSkill> choices = hero.getPossibleSkills();

        assertEquals(2, choices.size());
        assertEquals(1, countKnownSkills(hero, choices));
        assertEquals(1, countNewSkills(hero, choices));
    }

    @Test
    void shouldOfferSecondUpgradeableSkillWhenHeroCannotLearnMoreNewSkills() {
        EconomyHero hero = new EconomyHero();
        learnEightSkills(hero);

        List<AbstractSkill> choices = hero.getPossibleSkills();

        assertEquals(2, choices.size());
        assertEquals(2, countKnownSkills(hero, choices));
        assertEquals(2, choices.stream().map(AbstractSkill::getName).collect(Collectors.toSet()).size());
    }

    private void learnEightSkills(EconomyHero hero) {
        hero.upgradeSkill(new OffenceSkill());
        hero.upgradeSkill(new ArmorerSkill());
        hero.upgradeSkill(new LearningSkill());
        hero.upgradeSkill(new LogisticsSkill());
        hero.upgradeSkill(new TacticsSkill());
        hero.upgradeSkill(new AirMagicSkill());
        hero.upgradeSkill(new EarthMagicSkill());
        hero.upgradeSkill(new FireMagicSkill());
    }

    private long countKnownSkills(EconomyHero hero, List<AbstractSkill> choices) {
        Set<SkillName> learned = hero.getSkills().stream()
                .map(AbstractSkill::getName)
                .collect(Collectors.toSet());
        return choices.stream()
                .filter(choice -> learned.contains(choice.getName()))
                .count();
    }

    private long countNewSkills(EconomyHero hero, List<AbstractSkill> choices) {
        Set<SkillName> learned = hero.getSkills().stream()
                .map(AbstractSkill::getName)
                .collect(Collectors.toSet());
        return choices.stream()
                .filter(choice -> !learned.contains(choice.getName()))
                .count();
    }
}
