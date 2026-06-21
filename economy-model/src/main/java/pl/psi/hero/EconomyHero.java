package pl.psi.hero;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import pl.psi.creatures.EconomyCreature;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.artifacts.EconomySpell;
import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.HeroStatModifierManager;
import pl.psi.hero.skills.HeroStartingSkill;
import pl.psi.hero.skills.SkillChoiceManager;
import pl.psi.hero.skills.SkillFactory;
import pl.psi.hero.skills.SkillLevel;
import pl.psi.hero.skills.SkillRegistry;
import pl.psi.map.resources.Resources;

@Getter
@Setter
public class EconomyHero implements PropertyChangeListener
{
    private static final int MAX_SKILLS = 8;
    private static final int MIN_INITIAL_EXPERIENCE = 40;
    private static final int MAX_INITIAL_EXPERIENCE = 90;
    private static final String LEVEL_UP = "levelUp";

    private final Fraction fraction;
    private final HeroClass heroClass;
    private final List< EconomyCreature > creatureList;
    private Resources resources;
    private final int moveRange = 1000;
    private int remainingMoves;
    @Getter
    private int experience;
    public int level;
    @JsonIgnore
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private List<AbstractSkill> skills;
    private final Statistics baseStatistics;
    private final List<Artifact> artifacts = new ArrayList<>();
    private final List<EconomySpell> spells = new ArrayList<>();
    private final SkillFactory skillFactory = new SkillFactory();
    private final SkillRegistry skillRegistry = new SkillRegistry(skillFactory);
    private final SkillChoiceManager skillChoiceManager = new SkillChoiceManager(skillRegistry, skillFactory, new Random());
    private final HeroStatModifierManager heroStatModifierManager = new HeroStatModifierManager();

    public EconomyHero( final Fraction aFraction, final Resources aResources, final Statistics aStats)
    {
        this(aFraction, HeroClass.NECROMANCER, aResources, aStats);
    }

    public EconomyHero(final Fraction aFraction, final HeroClass aHeroClass, final Resources aResources, final Statistics aStats)
    {
        this(aFraction, aHeroClass, aResources, aStats, List.of());
    }

    public EconomyHero(
            final Fraction aFraction,
            final HeroClass aHeroClass,
            final Resources aResources,
            final Statistics aStats,
            final List<HeroStartingSkill> aStartingSkills)
    {
        fraction = aFraction;
        heroClass = aHeroClass;
        creatureList = new ArrayList<>();
        remainingMoves = moveRange;
        resources = aResources;
        baseStatistics = aStats;
        skills = new ArrayList<>();
        initializeStartingSkills(aStartingSkills);
        experience = ThreadLocalRandom.current().nextInt(MIN_INITIAL_EXPERIENCE, MAX_INITIAL_EXPERIENCE + 1);
    }

    public EconomyHero() {
        this.fraction = Fraction.NECROPOLIS;
        // Legacy/default constructor uses Necromancer because only Necropolis exists in the current economy model.
        this.heroClass = HeroClass.NECROMANCER;
        this.creatureList = new ArrayList<>();
        this.resources = new Resources(0, 0, 0, 0, 0, 0, 0);
        this.baseStatistics = new Statistics(0, 0, 0, 0);
        this.skills = new ArrayList<>();
        this.remainingMoves = moveRange;
    }

    public void resetMoveRange() {
        this.remainingMoves = getEffectiveMoveRange();
    }

    public boolean canMoveTo(double distance) {
        return distance <= remainingMoves;
    }

    public void deductMove(double distance) {
        remainingMoves -= distance;
    }

    public int getRemainingMoveRange() {
        return remainingMoves;
    }

    public int getEffectiveMoveRange() {
        return heroStatModifierManager.applyMovement(this, moveRange);
    }

    public void addCreature(final EconomyCreature aCreature)
    {
        if( creatureList.size() >= 7 )
        {
            throw new IllegalStateException( "Hero has not empty slot for creature" );
        }
        creatureList.add( aCreature );
    }

    public void addResource(final Resources changedResources) {
        Resources oldResources = this.resources;
        this.resources = this.resources.change(changedResources);
        pcs.firePropertyChange("resources", oldResources, this.resources);
    }

    public boolean canAfford(Resources cost) {
        return resources.enoughToPay(cost);
    }

    public boolean canAffordGold(int cost) {
        return resources.enoughToPayGold(cost);
    }

    public void pay(Resources cost) {
        if (!canAfford(cost)) {
            throw new IllegalStateException("Not enough resources");
        }
        this.resources = this.resources.change(cost.pay()); // pay() returns the negative values
    }

    public void payGold(int cost) {
        if (!canAffordGold(cost)) {
            throw new IllegalStateException("Not enough resources");
        }
        this.resources = this.resources.change(new Resources(-cost,0,0,0,0,0,0));
    }

    @JsonIgnore
    public List< EconomyCreature > getCreatures()
    {
        return List.copyOf( creatureList );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }

    public void upgradeSkill(AbstractSkill aSelectedSkill) {
        Optional<AbstractSkill> existing = skills.stream()
                .filter(s -> s.getName().equals(aSelectedSkill.getName()))
                .findFirst();

        if (existing.isPresent()) {
            if (existing.get().getLevel() != SkillLevel.EXPERT) {
                existing.get().upgrade();
            }
        } else {
            if (skills.size() >= MAX_SKILLS) {
                throw new IllegalStateException("Hero cannot learn more than " + MAX_SKILLS + " skills.");
            }
            skills.add(aSelectedSkill);
        }
    }

    public void initializeStartingSkills(List<HeroStartingSkill> startingSkills) {
        if (startingSkills == null) {
            return;
        }
        for (HeroStartingSkill startingSkill : startingSkills) {
            if (skills.stream().anyMatch(skill -> skill.getName().equals(startingSkill.getName()))) {
                continue;
            }
            if (skills.size() >= MAX_SKILLS) {
                throw new IllegalStateException("Hero cannot learn more than " + MAX_SKILLS + " skills.");
            }
            AbstractSkill skill = skillFactory.create(startingSkill.getName(), startingSkill.getLevel());
            skills.add(skill);
        }
    }

    public List<AbstractSkill> getPossibleSkills() {
        return skillChoiceManager.getPossibleSkills(this);
    }

    public void addSpell(EconomySpell aPickableSpell) {
        spells.add(aPickableSpell);
    }


    public enum Fraction
    {
        NECROPOLIS
    }

    public void addExperience(final int baseExperienceToAdd) {
        if (baseExperienceToAdd <= 0) {
            return;
        }

        int actualExperienceToAdd = heroStatModifierManager.applyExperience(this, baseExperienceToAdd);

        int oldLevel = this.level;
        this.experience += actualExperienceToAdd;

        // 3. Sprawdzanie awansu na nowy poziom
        while (this.experience >= getExperienceForNextLevel(this.level + 1)) {
            this.experience -= getExperienceForNextLevel(this.level + 1);
            this.level++;
            pcs.firePropertyChange(LEVEL_UP, oldLevel, this.level);
            oldLevel = this.level;
        }
    }

    private int getExperienceForNextLevel(final int nextLevel) {
        if (nextLevel <= 1) {
            return 0;
        }
        if (nextLevel == 2) {
            return 1000;
        }
        if (nextLevel == 3) {
            return 1000;
        }
        if (nextLevel == 4) {
            return 1200;
        }
        if (nextLevel == 5) {
            return 1400;
        }
        if (nextLevel == 6) {
            return 1600;
        }
        if (nextLevel == 7) {
            return 1800;
        }
        if (nextLevel == 8) {
            return 2000;
        }
        if (nextLevel == 9) {
            return 2200;
        }
        if (nextLevel == 10) {
            return 2500;
        }
        if (nextLevel == 11) {
            return 2800;
        }
        if (nextLevel == 12) {
            return 3100;
        }
        if (nextLevel == 13) {
            return 3720;
        }

        double requirement = 3720;
        for (int level = 14; level <= nextLevel; level++) {
            requirement *= 1.2;
        }
        return (int) Math.round(requirement);
    }

    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
    }

    @JsonIgnore
    public List<Artifact> getArtifacts() {
        return List.copyOf(artifacts);
    }

    @JsonIgnore
    public Statistics getTotalStatistics() {
        Statistics total = new Statistics(
                baseStatistics.getAttack(),
                baseStatistics.getDefense(),
                baseStatistics.getPower(),
                baseStatistics.getKnowledge()
        );
        for (Artifact artifact : artifacts) {
            total.increase(artifact.getType().getStatistics());
        }
        return total;
    }

    public int getAttack() {
        return getTotalStatistics().getAttack();
    }

    public int getDefense() {
        return getTotalStatistics().getDefense();
    }

    public int getPower() {
        return getTotalStatistics().getPower();
    }

    public int getKnowledge() {
        return getTotalStatistics().getKnowledge();
    }

    public void addObserver(PropertyChangeListener observer) {
        pcs.addPropertyChangeListener(observer);
    }
}
