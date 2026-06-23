package pl.psi.hero;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import pl.psi.creatures.EconomyCreature;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.artifacts.ArtifactType;
import pl.psi.hero.artifacts.EconomySpell;
import pl.psi.hero.skills.AbstractSkill;
import pl.psi.hero.skills.ArmorerSkill;
import pl.psi.hero.skills.OffenceSkill;
import pl.psi.map.resources.Resources;

@Getter
@Setter
public class EconomyHero implements PropertyChangeListener
{
    private static final int MIN_INITIAL_EXPERIENCE = 40;
    private static final int MAX_INITIAL_EXPERIENCE = 90;
    private static final String LEVEL_UP = "levelUp";

    private final Fraction fraction;
    private final List< EconomyCreature > creatureList;
    private Resources resources;
    private final int moveRange = 10;
    private int remainingMoves;
    private int experience;
    public int level;
    @JsonIgnore
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private List<AbstractSkill> skills;
    private final Statistics baseStatistics;
    private final List<Artifact> artifacts = new ArrayList<>();
    private final List<EconomySpell> spells = new ArrayList<>();
    protected List<ExpModifierIf> expModifiers = new ArrayList<>();
    private boolean hasGrail;
    private boolean hasDugThisTurn;

    public EconomyHero( final Fraction aFraction, final Resources aResources, final Statistics aStats)
    {
        fraction = aFraction;
        creatureList = new ArrayList<>();
        resources = aResources;
        baseStatistics = aStats;
        skills = new ArrayList<>();
        this.level = 1;
        remainingMoves = getMaxMoveRange();
        experience = ThreadLocalRandom.current().nextInt(MIN_INITIAL_EXPERIENCE, MAX_INITIAL_EXPERIENCE + 1);
    }
    public EconomyHero() {
        this.fraction = Fraction.NECROPOLIS;
        this.creatureList = new ArrayList<>();
        this.resources = new Resources(0, 0, 0, 0, 0, 0, 0);
        this.baseStatistics = new Statistics(0, 0, 0, 0);
        this.skills = new ArrayList<>();
        this.level = 1;
        this.remainingMoves = getMaxMoveRange();
    }

    public void resetMoveRange() {
        this.remainingMoves = getMaxMoveRange();
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
        resetMoveRange(); // Recalculate move range after adding a creature (which could be the slowest)
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
        skills.add(aSelectedSkill);
        if (aSelectedSkill instanceof ExpModifierIf) {
            addExpModifier((ExpModifierIf) aSelectedSkill);
        }
        resetMoveRange(); // Logistics or Scouting might have changed
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

    public void addSkill(final AbstractSkill aSkill) {
        if (skills == null) {
            skills = new ArrayList<>();
        }
        skills.add(aSkill);
        if (aSkill instanceof ExpModifierIf) {
            addExpModifier((ExpModifierIf) aSkill);
        }
        resetMoveRange();
    }

    public int getSlowestCreatureSpeed() {
        if (creatureList.isEmpty()) {
            return 6; // default
        }
        return creatureList.stream()
                .mapToInt(c -> c.getStats().getMoveRange())
                .min()
                .orElse(6);
    }

    public int getMaxMoveRange() {
        int slowestSpeed = getSlowestCreatureSpeed();
        int base = 10;
        if (slowestSpeed < 6) {
            base = 10 - (6 - slowestSpeed);
        } else {
            base = 10 + (slowestSpeed - 6) / 2;
        }

        double logisticsMultiplier = 1.0;
        if (skills != null) {
            for (AbstractSkill skill : skills) {
                if (skill.getName().name().equals("LOGISTICS")) {
                    logisticsMultiplier += skill.getFactor();
                }
            }
        }

        int artifactBonus = 0;
        if (artifacts != null) {
            for (Artifact art : artifacts) {
                if (art.getType() == ArtifactType.BOOTS_OF_SPEED) {
                    artifactBonus += 2;
                } else if (art.getType() == ArtifactType.EQUESTRIANS_GLOVES) {
                    artifactBonus += 3;
                }
            }
        }

        return (int) Math.round(base * logisticsMultiplier) + artifactBonus;
    }

    public int getVisibilityRadius() {
        int radius = 4;
        if (skills != null) {
            for (AbstractSkill skill : skills) {
                if (skill.getName().name().equals("SCOUTING")) {
                    radius += (int) skill.getFactor();
                }
            }
        }
        if (artifacts != null) {
            for (Artifact art : artifacts) {
                if (art.getType() == ArtifactType.SPYGLASS) {
                    radius += 1;
                } else if (art.getType() == ArtifactType.SPECULUM) {
                    radius += 2;
                }
            }
        }
        return radius;
    }

    public void generateResourcesFromArtifacts() {
        if (artifacts != null) {
            for (Artifact art : artifacts) {
                if (art.getType() == ArtifactType.ENDLESS_BAG_OF_GOLD) {
                    addResource(new Resources(750, 0, 0, 0, 0, 0, 0));
                } else if (art.getType() == ArtifactType.CHARCOAL_CART) {
                    addResource(new Resources(0, 0, 1, 0, 0, 0, 0));
                }
            }
        }
    }

    public boolean isHasGrail() {
        return hasGrail;
    }

    public void setHasGrail(boolean hasGrail) {
        this.hasGrail = hasGrail;
    }
}
