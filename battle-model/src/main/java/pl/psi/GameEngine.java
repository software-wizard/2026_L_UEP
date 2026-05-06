package pl.psi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.BiMap;
import pl.psi.BattleResults.BattleResult;
import pl.psi.BattleResults.OutcomeType;
import pl.psi.BattleResults.WinnerSide;
import pl.psi.Spells.Spell;
import pl.psi.creatures.Creature;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * TODO: Describe this class (The first line - until the first dot - will interpret as the brief description).
 */
public class GameEngine {

    public static final String CREATURE_MOVED = "CREATURE_MOVED";
    public static final String BATTLE_FINISHED = "BATTLE_FINISHED";
    private static final String SPELL_CAST = "SPELL_CAST";
    private final TurnQueue turnQueue;
    private Board board;
    @JsonIgnore
    private final PropertyChangeSupport observerSupport = new PropertyChangeSupport(this);
    private final Hero hero1;
    private final Hero hero2;
    private final Map<Creature, Hero> customOwners;
    private int hero1VanquishedEnemyHp;
    private int hero2VanquishedEnemyHp;
    private BattleResult battleResult;

    public GameEngine(final Hero aHero1, final Hero aHero2) {
        this.hero1 = aHero1;
        this.hero2 = aHero2;
        turnQueue = new TurnQueue(aHero1.getCreatures(), aHero2.getCreatures());
        board = new Board(aHero1.getCreatures(), aHero2.getCreatures());
        turnQueue.addObserver(hero1);
        turnQueue.addObserver(hero2);
        customOwners = new IdentityHashMap<>();
    }

    public GameEngine(final Hero aHero1, final Hero aHero2, final BiMap<BattlePoint, SpecialField> specialFields, Map<BattlePoint, Creature> aBankEnemy) {
        this(aHero1, aHero2);
        board = new Board(aHero1.getCreatures(), aHero2.getCreatures(), specialFields, aBankEnemy);
        for (Creature creature : aBankEnemy.values()) {
            customOwners.put(creature, hero2);
        }
    }

    public void attack(final BattlePoint aBattlePoint) {
        if (isBattleOver()) {
            return;
        }

        board.getCreature(aBattlePoint)
                .ifPresent(defender -> {
                    Creature attacker = turnQueue.getCurrentCreature();
                    Hero attackerOwner = getOwnerOf(attacker);
                    Hero defenderOwner = getOwnerOf(defender);

                    int defenderHpBefore = calculateTotalHp(defender);
                    int attackerHpBefore = calculateTotalHp(attacker);

                    attacker.attack(defender);

                    int defenderHpLoss = Math.max(0, defenderHpBefore - calculateTotalHp(defender));
                    int attackerHpLoss = Math.max(0, attackerHpBefore - calculateTotalHp(attacker));
                    registerVanquishedHp(attackerOwner, defenderHpLoss);
                    registerVanquishedHp(defenderOwner, attackerHpLoss);

                    if (!defender.isAlive()) {
                        board.removeCreature(defender);
                        removeDeadCreature(defender);
                    }

                    if (!attacker.isAlive()) {
                        board.removeCreature(attacker);
                        removeDeadCreature(attacker);
                    }
                });

        updateBattleResultIfFinished();
        if (isBattleOver()) {
            return;
        }

        pass();
    }

    private void removeDeadCreature(Creature creature) {
        Hero owner = getOwnerOf(creature);
        owner.removeCreature(creature);
    }

    private Hero getOwnerOf(Creature creature) {
        if (hero1.getCreatures().contains(creature)) {
            return hero1;
        } else if (hero2.getCreatures().contains(creature)) {
            return hero2;
        } else if (customOwners.containsKey(creature)) {
            return customOwners.get(creature);
        } else {
            throw new IllegalStateException("Creature not owned by any hero");
        }
    }

    private int calculateTotalHp(final Creature creature) {
        if (!creature.isAlive()) {
            return 0;
        }
        return (creature.getAmount() - 1) * creature.getMaxHp() + creature.getCurrentHp();
    }

    private void registerVanquishedHp(final Hero owner, final int hp) {
        if (hp <= 0) {
            return;
        }
        if (owner == hero1) {
            hero1VanquishedEnemyHp += hp;
        } else if (owner == hero2) {
            hero2VanquishedEnemyHp += hp;
        }
    }

    private void updateBattleResultIfFinished() {
        if (battleResult != null) {
            return;
        }

        boolean hero1Defeated = hero1.getCreatures().isEmpty();
        boolean hero2Defeated = hero2.getCreatures().isEmpty();

        if (!hero1Defeated && !hero2Defeated) {
            return;
        }

        if (hero1Defeated && hero2Defeated) {
            battleResult = new BattleResult(null, OutcomeType.MUTUAL_DEFEAT, 0);
        } else if (hero1Defeated){
            battleResult = new BattleResult(hero2, OutcomeType.DEFEAT, hero2VanquishedEnemyHp);
        }else {
            battleResult = new BattleResult(hero1, OutcomeType.DEFEAT, hero1VanquishedEnemyHp);
        }

        observerSupport.firePropertyChange(BATTLE_FINISHED, null, battleResult);
    }

    public boolean isBattleOver() {
        return battleResult != null;
    }

    public Optional<BattleResult> getBattleResult() {
        return Optional.ofNullable(battleResult);
    }

    public boolean isHero1Winner() {
        return battleResult != null && battleResult.getWinner() == hero1;
    }


    public boolean canMove(final BattlePoint aBattlePoint) {
        if (isBattleOver()) {
            return false;
        }
        return board.canMove(turnQueue.getCurrentCreature(), aBattlePoint);
    }

    public void move(final BattlePoint aBattlePoint) {
        if (isBattleOver()) {
            return;
        }
        board.move(turnQueue.getCurrentCreature(), aBattlePoint);
        updateBattleResultIfFinished();
        observerSupport.firePropertyChange(CREATURE_MOVED, null, aBattlePoint);
    }

    public Optional<Creature> getCreature(final BattlePoint aBattlePoint) {
        return board.getCreature(aBattlePoint);
    }

    public void pass() {
        if (isBattleOver()) {
            return;
        }
        turnQueue.next();
    }

    public void addObserver(final PropertyChangeListener aObserver) {
        observerSupport.addPropertyChangeListener(aObserver);
        turnQueue.addObserver(aObserver);
    }

    public boolean canAttack(final BattlePoint aBattlePoint) {
        if (isBattleOver()) {
            return false;
        }
        double distance = board.getPosition(turnQueue.getCurrentCreature())
                .distance(aBattlePoint);
        return board.getCreature(aBattlePoint)
                .isPresent()
                && distance < 2 && distance > 0;
    }

    public boolean isCurrentCreature(BattlePoint aBattlePoint) {
        return Optional.of(turnQueue.getCurrentCreature()).equals(board.getCreature(aBattlePoint));
    }

    public BiMap<BattlePoint, SpecialField> getSpecialFields() {
        return board.getSpecialFields();
    }

    public void interact(BattlePoint aCurrentBattlePoint) {
        if (isBattleOver()) {
            return;
        }
        board.interact(turnQueue.getCurrentCreature(), aCurrentBattlePoint);
        updateBattleResultIfFinished();
    }


    public Hero getCurrentHero() {
        Creature current = turnQueue.getCurrentCreature();
        if (hero1.getCreatures().contains(current)) {
            return hero1;
        } else {
            return hero2;
        }
    }

    public void castSpell(Spell spell, Creature targetCreature) {
        if (isBattleOver()) {
            return;
        }
        getCurrentHero().castSpell(spell, targetCreature);
        updateBattleResultIfFinished();
        notifySpellCast(spell);
    }

    public void castSpell(Spell spell, BattlePoint targetPoint) {
        java.util.List<BattlePoint> areaPoints = spell.getAreaStrategy().getArea(targetPoint);
        java.util.List<Creature> affectedCreatures = board.getCreaturesFromPoints(areaPoints);

        getCurrentHero().castSpell(spell, affectedCreatures);

        notifySpellCast(spell);
    }

    private void notifySpellCast(Spell spell) {
        observerSupport.firePropertyChange(SPELL_CAST, null, spell);
    }

    public static class BuffField {
    }
}
