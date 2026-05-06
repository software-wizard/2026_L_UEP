package pl.psi.hero;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.Getter;
import pl.psi.creatures.EconomyCreature;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.artifacts.EconomySpell;
import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.ArmorerSkill;
import pl.psi.hero.skills.OffenceSkill;
import pl.psi.hero.skills.LearningSkill;
import pl.psi.map.resources.Resources;

public class EconomyHero implements PropertyChangeListener
{
    private final Fraction fraction;
    private final List< EconomyCreature > creatureList;
    @Getter
    private Resources resources;
    @Getter
    private final int moveRange = 10;
    private int remainingMoves;
    @Getter
    private int experience;
    @Getter
    public int level;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    @Getter
    private List<AbstractSkill> skills;

    private final Statistics baseStatistics;
    private final List<Artifact> artifacts = new ArrayList<>();
    @Getter
    private final List<EconomySpell> spells = new ArrayList<>();

    public EconomyHero( final Fraction aFraction, final Resources aResources, final Statistics aStats)
    {
        fraction = aFraction;
        creatureList = new ArrayList<>();
        remainingMoves = moveRange;
        resources = aResources;
        baseStatistics = aStats;
        skills = new ArrayList<>();
    }

    public void resetMoveRange() {
        this.remainingMoves = moveRange;
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
            existing.get().upgrade();
        } else {
            skills.add(aSelectedSkill);
        }
    }

    public List<AbstractSkill> getPossibleSkills() {
        List<AbstractSkill> possible = new ArrayList<>();
        // Simple logic for now: only Offence and Armorer
        addIfPossible(possible, pl.psi.hero.skills.SkillName.OFFENCE, new pl.psi.hero.skills.OffenceSkill());
        addIfPossible(possible, pl.psi.hero.skills.SkillName.ARMORER, new pl.psi.hero.skills.ArmorerSkill());
        addIfPossible(possible, pl.psi.hero.skills.SkillName.LEARNING, new pl.psi.hero.skills.LearningSkill());
        return possible;
    }

    private void addIfPossible(List<AbstractSkill> list, pl.psi.hero.skills.SkillName name, AbstractSkill skillTemplate) {
        Optional<AbstractSkill> existing = skills.stream().filter(s -> s.getName() == name).findFirst();
        if (existing.isEmpty() || existing.get().getLevel() != pl.psi.hero.skills.SkillLevel.EXPERT) {
            list.add(skillTemplate);
        }
    }

    public void addSpell(EconomySpell aPickableSpell) {
        spells.add(aPickableSpell);
    }


    public enum Fraction
    {
        NECROPOLIS
    }

    public void addExperience(final int experienceToAdd) {
        float bonus = 1.0f;
        Optional<AbstractSkill> learning = skills.stream()
                .filter(s -> s.getName() == pl.psi.hero.skills.SkillName.LEARNING)
                .findFirst();
        if (learning.isPresent()) {
            bonus += learning.get().getFactor();
        }

        int finalExperience = Math.round(experienceToAdd * bonus);
        int oldLevel = this.level;
        this.experience += finalExperience;

        while (this.experience >= getExperienceForNextLevel(this.level)) {
            this.experience -= getExperienceForNextLevel(this.level);
            this.level++;
            pcs.firePropertyChange("levelUp", oldLevel, this.level);
            pcs.firePropertyChange("levelUp_hero", null, this);
            oldLevel = this.level;
        }
    }

    private int getExperienceForNextLevel(int currentLevel) {
        return 100 + (currentLevel * 50);
    }

    public void addArtifact(Artifact artifact) {
        artifacts.add(artifact);
    }

    public List<Artifact> getArtifacts() {
        return List.copyOf(artifacts);
    }

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

    public void addSkill(final ArmorerSkill aSkill)
    {
        if( skills == null )
        {
            skills = new ArrayList<>();
        }
        skills.add( aSkill );
    }
    public void addSkill(final OffenceSkill aSkill)
    {
        if( skills == null )
        {
            skills = new ArrayList<>();
        }
        skills.add( aSkill );
    }
    public void addObserver(PropertyChangeListener observer) {
        pcs.addPropertyChangeListener(observer);
    }
}
