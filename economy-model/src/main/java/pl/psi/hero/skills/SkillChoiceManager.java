package pl.psi.hero.skills;

import pl.psi.hero.EconomyHero;
import pl.psi.hero.HeroClass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class SkillChoiceManager {
    private static final int MAX_SKILLS = 8;
    private static final int MAX_CHOICES = 2;

    private final SkillRegistry skillRegistry;
    private final SkillFactory skillFactory;
    private final Random random;
    private final SkillProbabilityTable probabilityTable;

    public SkillChoiceManager(SkillRegistry skillRegistry, SkillFactory skillFactory, Random random) {
        this(skillRegistry, skillFactory, random, SkillProbabilityTable.getDefault());
    }

    public SkillChoiceManager(
            SkillRegistry skillRegistry,
            SkillFactory skillFactory,
            Random random,
            SkillProbabilityTable probabilityTable) {
        this.skillRegistry = skillRegistry;
        this.skillFactory = skillFactory;
        this.random = random;
        this.probabilityTable = probabilityTable;
    }

    public List<AbstractSkill> getPossibleSkills(EconomyHero hero) {
        return getPossibleSkills(hero.getHeroClass(), hero.getSkills());
    }

    public List<AbstractSkill> getPossibleSkills(List<AbstractSkill> heroSkills) {
        return getPossibleSkills(HeroClass.NECROMANCER, heroSkills);
    }

    public List<AbstractSkill> getPossibleSkills(HeroClass heroClass, List<AbstractSkill> heroSkills) {
        List<WeightedSkillCandidate> upgradeCandidates = new ArrayList<>();
        List<WeightedSkillCandidate> newCandidates = new ArrayList<>();
        Set<SkillName> learnedSkillNames = new HashSet<>();
        Set<SkillName> upgradeCandidateNames = new HashSet<>();
        Set<SkillName> newCandidateNames = new HashSet<>();

        for (AbstractSkill skill : heroSkills) {
            learnedSkillNames.add(skill.getName());
        }

        for (SkillName skillName : skillRegistry.getAvailableSkillNames()) {
            Optional<AbstractSkill> existing = heroSkills.stream()
                    .filter(skill -> skill.getName().equals(skillName))
                    .findFirst();

            if (existing.isPresent()) {
                if (existing.get().getLevel() != SkillLevel.EXPERT) {
                    if (upgradeCandidateNames.add(skillName)) {
                        int weight = probabilityTable.getWeight(heroClass, skillName);
                        upgradeCandidates.add(new WeightedSkillCandidate(skillName, Math.max(weight, 1)));
                    }
                }
            } else if (learnedSkillNames.size() < MAX_SKILLS) {
                int weight = probabilityTable.getWeight(heroClass, skillName);
                if (weight > 0 && newCandidateNames.add(skillName)) {
                    newCandidates.add(new WeightedSkillCandidate(skillName, weight));
                }
            }
        }

        List<SkillName> selectedSkillNames = selectSkillNames(upgradeCandidates, newCandidates);
        List<AbstractSkill> choices = new ArrayList<>();
        for (SkillName skillName : selectedSkillNames) {
            choices.add(skillFactory.create(skillName));
        }
        return choices;
    }

    private List<SkillName> selectSkillNames(List<WeightedSkillCandidate> upgradeCandidates, List<WeightedSkillCandidate> newCandidates) {
        List<SkillName> selected = new ArrayList<>();

        if (!upgradeCandidates.isEmpty() && !newCandidates.isEmpty()) {
            addUnique(selected, chooseWeighted(upgradeCandidates));
            addUnique(selected, chooseWeighted(newCandidates));
            return selected;
        }

        if (upgradeCandidates.isEmpty()) {
            addUpToTwo(selected, newCandidates);
            return selected;
        }

        addUpToTwo(selected, upgradeCandidates);
        return selected;
    }

    private void addUpToTwo(List<SkillName> selected, List<WeightedSkillCandidate> candidates) {
        List<WeightedSkillCandidate> remaining = new ArrayList<>(candidates);
        while (!remaining.isEmpty()) {
            SkillName candidate = chooseWeighted(remaining);
            addUnique(selected, candidate);
            remaining.removeIf(skillCandidate -> skillCandidate.skillName.equals(candidate));
            if (selected.size() == MAX_CHOICES) {
                return;
            }
        }
    }

    private void addUnique(List<SkillName> selected, SkillName skillName) {
        if (!selected.contains(skillName)) {
            selected.add(skillName);
        }
    }

    private SkillName chooseWeighted(List<WeightedSkillCandidate> candidates) {
        int totalWeight = 0;
        for (WeightedSkillCandidate candidate : candidates) {
            totalWeight += candidate.weight;
        }
        int selectedWeight = random.nextInt(totalWeight);
        int cumulativeWeight = 0;
        for (WeightedSkillCandidate candidate : candidates) {
            cumulativeWeight += candidate.weight;
            if (selectedWeight < cumulativeWeight) {
                return candidate.skillName;
            }
        }
        return candidates.get(candidates.size() - 1).skillName;
    }

    private static class WeightedSkillCandidate {
        private final SkillName skillName;
        private final int weight;

        private WeightedSkillCandidate(SkillName skillName, int weight) {
            this.skillName = skillName;
            this.weight = weight;
        }
    }
}
