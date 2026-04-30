package pl.psi.hero;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import pl.psi.creatures.EconomyCreature;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.artifacts.EconomySpell;
import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.ArmorerSkill;
import pl.psi.hero.skills.OffenceSkill;
import pl.psi.map.resources.Resources;

public class EconomyHero implements PropertyChangeListener
{
    private static final int MIN_INITIAL_EXPERIENCE = 40;
    private static final int MAX_INITIAL_EXPERIENCE = 90;
    
    private static final String LEVEL_UP = "levelUp";

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
    protected List<ExpModifierIf> expModifiers = new ArrayList<>();

    public EconomyHero( final Fraction aFraction, final Resources aResources, final Statistics aStats)
    {
        fraction = aFraction;
        creatureList = new ArrayList<>();
        remainingMoves = moveRange;
        resources = aResources;
        baseStatistics = aStats;
        skills = new ArrayList<>();
        experience = ThreadLocalRandom.current().nextInt(MIN_INITIAL_EXPERIENCE, MAX_INITIAL_EXPERIENCE + 1);
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
        skills.add(aSelectedSkill);
    }

    public void addSpell(EconomySpell aPickableSpell) {
        spells.add(aPickableSpell);
    }


    public enum Fraction
    {
        NECROPOLIS
    }

    protected void addExpModifier(ExpModifierIf modifier){
        expModifiers.add(modifier);
    }

    protected void removeExpModifier(ExpModifierIf modifier){
        expModifiers.remove(modifier);
    }

    public void addExperience(final int baseExperienceToAdd) {
        if (baseExperienceToAdd <= 0) {
            return;
        }

        double totalMultiplier = expModifiers.stream()
                .map(ExpModifierIf::getExpMultiplier)
                .reduce(1.0, (a, b) -> a * b);

        // Alternatywa: Jeśli wolisz dodawać bonusy (np. +5% i +10% daje +15%, a nie 1.05 * 1.10):
        // double totalMultiplier = 1.0 + expModifiers.stream()
        //         .mapToDouble(m -> m.getExpMultiplier() - 1.0)
        //         .sum();

        // 2. Aplikowanie zmian i zaokrąglanie
        int actualExperienceToAdd = (int) Math.round(baseExperienceToAdd * totalMultiplier);

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
}
