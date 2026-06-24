package pl.psi.creatures;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lombok.AccessLevel;
import lombok.Setter;
import pl.psi.Spells.ActiveSpellEffect;
import pl.psi.Spells.BuffSpell;
import pl.psi.Spells.Spell;
import pl.psi.TurnQueue;

import com.google.common.collect.Range;

import lombok.Getter;

@Getter
public class Creature implements PropertyChangeListener {
    private CreatureStatisticIf stats;
    private CreatureStatisticIf originalStats;
    @Setter
    private int amount;

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int currentHp;
    private int counterAttackCounter = 1;
    private DamageCalculatorIf calculator;
    private final List<ActiveSpellEffect> activeSpellEffects = new ArrayList<>();
    private float reduceDemegeFactor;
    private int remainingMovePoints;

    // Rooting: Treeman/Ent tracks which creature it has rooted.
    // Root lifts when this creature moves or dies.
    private Creature rootedCreature = null;

    Creature() {
    }

    private Creature(final CreatureStatisticIf aStats, final DamageCalculatorIf aCalculator,
                     final int aAmount) {
        stats = aStats;
        amount = aAmount;
        currentHp = stats.getMaxHp();
        calculator = aCalculator;
        reduceDemegeFactor = 1;
        this.originalStats = aStats;
        remainingMovePoints = aStats.getMoveRange();
    }

    public void attack(final Creature aDefender) {
        if (!isAlive()) return;

        performSingleAttack(aDefender);

        // Double attacker (High Elf) strikes again if defender survived.
        // Ranged units never provoke counter-attack — handled inside canCounterAttack().
        if (stats.isDoubleAttacker() && aDefender.isAlive()) {
            performSingleAttack(aDefender);
        }
    }

    private void performSingleAttack(final Creature aDefender) {
        final int damage = getCalculator().calculateDamage(this, aDefender);
        applyDamage(aDefender, damage);

        // Apply root after hit if this is a Treeman/Ent and defender survived
        if (isRooter() && aDefender.isAlive()) {
            applyRoot(aDefender);
        }

        if (canCounterAttack(aDefender)) {
            counterAttack(aDefender);
        }
    }

    // Called by Board when this creature moves — releases the root
    public void onMove() {
        releaseRoot();
    }

    // Called by GameEngine when this creature dies — releases the root
    public void onDeath() {
        releaseRoot();
    }

    private void applyRoot(Creature aTarget) {
        // Release any previous root before applying a new one
        releaseRoot();
        rootedCreature = aTarget;
        aTarget.remainingMovePoints = 0;
    }

    private void releaseRoot() {
        if (rootedCreature != null && rootedCreature.isAlive()) {
            rootedCreature.remainingMovePoints = rootedCreature.stats.getMoveRange();
        }
        rootedCreature = null;
    }

    // Treeman and Ent are rooters — identified by stat name
    public boolean isRooter() {
        String name = stats.getName();
        return name.equals("Treeman") || name.equals("Ent");
    }

    public boolean isRanged() {
        return stats.isRanged();
    }

    public boolean isDoubleAttacker() {
        return stats.isDoubleAttacker();
    }

    public boolean isAlive() {
        return getAmount() > 0;
    }

    public void applyDamage(final Creature aDefender, final int aDamage) {
        int totalHp = (aDefender.getAmount() - 1) * aDefender.getMaxHp() + aDefender.getCurrentHp();
        int remainingHp = totalHp - aDamage;

        if (remainingHp <= 0) {
            aDefender.setAmount(0);
            aDefender.setCurrentHp(0);
            return;
        }

        int newAmount = (remainingHp - 1) / aDefender.getMaxHp() + 1;
        int newCurrentHp = remainingHp - (newAmount - 1) * aDefender.getMaxHp();
        aDefender.setAmount(newAmount);
        aDefender.setCurrentHp(newCurrentHp);
    }

    public int getMaxHp() {
        return stats.getMaxHp();
    }

    private boolean canCounterAttack(final Creature aDefender) {
        // Ranged attackers never receive a counter-attack
        if (isRanged()) return false;
        return aDefender.getCounterAttackCounter() > 0 && aDefender.getCurrentHp() > 0;
    }

    private void counterAttack(final Creature aAttacker) {
        final int damage = aAttacker.getCalculator()
                .calculateDamage(aAttacker, this);
        applyDamage(this, damage);
        aAttacker.counterAttackCounter--;
    }

    Range<Integer> getDamage() {
        return stats.getDamage();
    }

    public int getAttack() {
        return stats.getAttack();
    }

    public void applyTemporaryBuff(BuffSpell buffSpell) {
        this.getActiveSpellEffects().add(new ActiveSpellEffect(buffSpell, buffSpell.getDuration()));

        CreatureStatisticIf modifiedStats;
        if (originalStats instanceof CreatureStats) {
            modifiedStats = new CreatureStats((CreatureStats) originalStats);
        } else {
            modifiedStats = originalStats;
        }

        for (ActiveSpellEffect effect : activeSpellEffects) {
            modifiedStats = effect.getSpell().modifyStats(modifiedStats);
        }
        this.stats = modifiedStats;
    }

    public int getArmor() {
        return stats.getArmor();
    }

    public int getRemainingMovePoints() {
        return remainingMovePoints;
    }

    public void reduceMovePoints(double distance) {
        remainingMovePoints -= (int) Math.ceil(distance);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (TurnQueue.END_OF_TURN.equals(evt.getPropertyName())) {
            counterAttackCounter = 1;
            remainingMovePoints = stats.getMoveRange();
            updateActiveSpells();
        }
    }

    private void updateActiveSpells() {
        Iterator<ActiveSpellEffect> iterator = activeSpellEffects.iterator();
        while (iterator.hasNext()) {
            ActiveSpellEffect effect = iterator.next();
            effect.decreaseDuration();
            if (effect.isExpired()) {
                iterator.remove();
            }
        }

        CreatureStatisticIf modifiedStats;
        if (originalStats instanceof CreatureStats) {
            modifiedStats = new CreatureStats((CreatureStats) originalStats);
        } else {
            modifiedStats = originalStats;
        }

        for (ActiveSpellEffect effect : activeSpellEffects) {
            modifiedStats = effect.getSpell().modifyStats(modifiedStats);
        }
        this.stats = modifiedStats;
    }

    protected void restoreCurrentHpToMax() {
        currentHp = stats.getMaxHp();
    }

    public String getName() {
        return stats.getName();
    }

    public int getMoveRange() {
        return stats.getMoveRange();
    }

    public boolean isFlying() {
        return stats.getMovementType() == MovementType.FLYING;
    }

    public void applyMagicDamage(Spell aDamageSpell) {
        if (isAlive()) {
            final int magicDamage = getCalculator().calculateMagicDamage(this, aDamageSpell);
            applyDamage(this, magicDamage);
        }
    }

    public static class Builder {
        private int amount = 1;
        private DamageCalculatorIf calculator = new DefaultDamageCalculator(new Random());
        private CreatureStatisticIf statistic;

        public Builder statistic(final CreatureStatisticIf aStatistic) {
            statistic = aStatistic;
            return this;
        }

        public Builder amount(final int aAmount) {
            amount = aAmount;
            return this;
        }

        public Builder reduceDemegeFactor(float aReduceDemegeFactor) {
            return this;
        }

        public Builder calculator(final DamageCalculatorIf aCalc) {
            calculator = aCalc;
            return this;
        }

        public Creature build() {
            return new Creature(statistic, calculator, amount);
        }
    }

    @Override
    public String toString() {
        return getName() + System.lineSeparator() + getAmount();
    }
}