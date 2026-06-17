package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;
import pl.psi.hero.HeroClass;

import java.io.StringReader;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillChoiceManagerTest {
    private final SkillFactory skillFactory = new SkillFactory();

    @Test
    void returnsOneUpgradeAndOneNewSkillWhenBothGroupsAreAvailable() {
        SkillChoiceManager manager = defaultManager();
        List<AbstractSkill> heroSkills = List.of(skillFactory.create(SkillName.OFFENCE));

        List<AbstractSkill> choices = manager.getPossibleSkills(heroSkills);

        assertEquals(2, choices.size());
        assertEquals(1, countKnownSkills(heroSkills, choices));
        assertEquals(1, countNewSkills(heroSkills, choices));
    }

    @Test
    void returnsOnlyUpgradesWhenHeroHasEightSkills() {
        SkillChoiceManager manager = defaultManager();
        List<AbstractSkill> heroSkills = List.of(
                skillFactory.create(SkillName.OFFENCE),
                skillFactory.create(SkillName.ARMORER),
                skillFactory.create(SkillName.LEARNING),
                skillFactory.create(SkillName.LOGISTICS),
                skillFactory.create(SkillName.TACTICS),
                skillFactory.create(SkillName.AIR_MAGIC),
                skillFactory.create(SkillName.EARTH_MAGIC),
                skillFactory.create(SkillName.FIRE_MAGIC)
        );

        List<AbstractSkill> choices = manager.getPossibleSkills(heroSkills);

        assertEquals(2, choices.size());
        assertEquals(2, countKnownSkills(heroSkills, choices));
    }

    @Test
    void skipsExpertSkills() {
        SkillChoiceManager manager = managerFor(List.of(SkillName.OFFENCE, SkillName.ARMORER));
        List<AbstractSkill> heroSkills = List.of(
                skillFactory.create(SkillName.OFFENCE, SkillLevel.EXPERT),
                skillFactory.create(SkillName.ARMORER)
        );

        List<AbstractSkill> choices = manager.getPossibleSkills(heroSkills);

        assertEquals(1, choices.size());
        assertEquals(SkillName.ARMORER, choices.get(0).getName());
        assertFalse(choices.stream().anyMatch(skill -> skill.getName().equals(SkillName.OFFENCE)));
    }

    @Test
    void doesNotReturnDuplicateSkillNames() {
        SkillChoiceManager manager = managerFor(List.of(SkillName.OFFENCE, SkillName.OFFENCE, SkillName.ARMORER));

        List<AbstractSkill> choices = manager.getPossibleSkills(List.of());

        assertEquals(2, choices.size());
        assertEquals(choices.size(), choices.stream().map(AbstractSkill::getName).collect(Collectors.toSet()).size());
    }

    @Test
    void returnsOneChoiceWhenThereIsOnlyOneCandidate() {
        SkillChoiceManager manager = managerFor(List.of(SkillName.OFFENCE));

        List<AbstractSkill> choices = manager.getPossibleSkills(List.of());

        assertEquals(1, choices.size());
        assertEquals(SkillName.OFFENCE, choices.get(0).getName());
    }

    @Test
    void returnsEmptyListWhenThereAreNoCandidates() {
        SkillChoiceManager manager = managerFor(List.of(SkillName.OFFENCE));
        List<AbstractSkill> heroSkills = List.of(skillFactory.create(SkillName.OFFENCE, SkillLevel.EXPERT));

        List<AbstractSkill> choices = manager.getPossibleSkills(heroSkills);

        assertTrue(choices.isEmpty());
    }

    @Test
    void doesNotChooseNewSkillWithZeroWeight() {
        SkillChoiceManager manager = managerFor(
                List.of(SkillName.OFFENCE, SkillName.ARMORER),
                weights(Map.of(SkillName.OFFENCE, 0, SkillName.ARMORER, 1))
        );

        List<AbstractSkill> choices = manager.getPossibleSkills(HeroClass.KNIGHT, List.of());

        assertEquals(1, choices.size());
        assertEquals(SkillName.ARMORER, choices.get(0).getName());
    }

    @Test
    void canUpgradeOwnedSkillWithZeroWeight() {
        SkillChoiceManager manager = managerFor(
                List.of(SkillName.OFFENCE, SkillName.ARMORER),
                weights(Map.of(SkillName.OFFENCE, 0, SkillName.ARMORER, 0))
        );

        List<AbstractSkill> choices = manager.getPossibleSkills(
                HeroClass.KNIGHT,
                List.of(skillFactory.create(SkillName.OFFENCE))
        );

        assertEquals(1, choices.size());
        assertEquals(SkillName.OFFENCE, choices.get(0).getName());
    }

    @Test
    void higherWeightIsChosenMoreOften() {
        SkillProbabilityTable table = weights(Map.of(
                SkillName.OFFENCE, 20,
                SkillName.ARMORER, 1,
                SkillName.LEARNING, 1
        ));
        SkillChoiceManager manager = managerFor(
                List.of(SkillName.OFFENCE, SkillName.ARMORER, SkillName.LEARNING),
                table
        );

        int offenceFirstChoices = 0;
        int armorerFirstChoices = 0;
        for (int i = 0; i < 1000; i++) {
            List<AbstractSkill> choices = manager.getPossibleSkills(HeroClass.KNIGHT, List.of());
            if (choices.get(0).getName().equals(SkillName.OFFENCE)) {
                offenceFirstChoices++;
            }
            if (choices.get(0).getName().equals(SkillName.ARMORER)) {
                armorerFirstChoices++;
            }
        }

        assertTrue(offenceFirstChoices > armorerFirstChoices);
    }

    @Test
    void returnsDeterministicChoicesForTheSameSeed() {
        SkillChoiceManager firstManager = managerFor(
                List.of(SkillName.OFFENCE, SkillName.ARMORER, SkillName.LEARNING),
                weights(Map.of(SkillName.OFFENCE, 5, SkillName.ARMORER, 3, SkillName.LEARNING, 1))
        );
        SkillChoiceManager secondManager = managerFor(
                List.of(SkillName.OFFENCE, SkillName.ARMORER, SkillName.LEARNING),
                weights(Map.of(SkillName.OFFENCE, 5, SkillName.ARMORER, 3, SkillName.LEARNING, 1))
        );

        List<SkillName> firstChoices = firstManager.getPossibleSkills(HeroClass.KNIGHT, List.of()).stream()
                .map(AbstractSkill::getName)
                .collect(Collectors.toList());
        List<SkillName> secondChoices = secondManager.getPossibleSkills(HeroClass.KNIGHT, List.of()).stream()
                .map(AbstractSkill::getName)
                .collect(Collectors.toList());

        assertEquals(firstChoices, secondChoices);
    }

    private SkillChoiceManager defaultManager() {
        return new SkillChoiceManager(new SkillRegistry(skillFactory), skillFactory, new Random(0));
    }

    private SkillChoiceManager managerFor(List<SkillName> skillNames) {
        return new SkillChoiceManager(new SkillRegistry(skillNames, skillFactory), skillFactory, new Random(0));
    }

    private SkillChoiceManager managerFor(List<SkillName> skillNames, SkillProbabilityTable table) {
        return new SkillChoiceManager(new SkillRegistry(skillNames, skillFactory), skillFactory, new Random(0), table);
    }

    private SkillProbabilityTable weights(Map<SkillName, Integer> knightWeights) {
        Map<SkillName, Integer> weights = new EnumMap<>(SkillName.class);
        weights.putAll(knightWeights);

        StringBuilder csv = new StringBuilder("skill");
        for (HeroClass heroClass : HeroClass.values()) {
            csv.append(",").append(heroClass.name());
        }
        csv.append("\n");

        for (SkillName skillName : SkillName.values()) {
            csv.append(skillName.name());
            for (HeroClass heroClass : HeroClass.values()) {
                int weight = heroClass == HeroClass.KNIGHT ? weights.getOrDefault(skillName, 1) : 1;
                csv.append(",").append(weight);
            }
            csv.append("\n");
        }
        return new SkillProbabilityTable(new StringReader(csv.toString()));
    }

    private long countKnownSkills(List<AbstractSkill> heroSkills, List<AbstractSkill> choices) {
        Set<SkillName> learned = heroSkills.stream()
                .map(AbstractSkill::getName)
                .collect(Collectors.toSet());
        return choices.stream()
                .filter(choice -> learned.contains(choice.getName()))
                .count();
    }

    private long countNewSkills(List<AbstractSkill> heroSkills, List<AbstractSkill> choices) {
        Set<SkillName> learned = heroSkills.stream()
                .map(AbstractSkill::getName)
                .collect(Collectors.toSet());
        return choices.stream()
                .filter(choice -> !learned.contains(choice.getName()))
                .count();
    }
}
