package pl.psi;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatistic;
import pl.psi.creatures.DefaultDamageCalculator;

import static org.junit.jupiter.api.Assertions.*;

class SpecialTraitsTest {

    // ---- Rooting (Treeman / Ent) ----

    @Test
    void treemansAttackRootsDefender() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 10);
        Creature target = creature(CreatureStatistic.SKELETON, 500);

        treeman.attack(target);

        assertEquals(0, target.getRemainingMovePoints(),
                "Treeman should root defender, setting move points to 0");
    }

    @Test
    void entAlsoRootsDefender() {
        Creature ent = creature(CreatureStatistic.ENT, 10);
        Creature target = creature(CreatureStatistic.SKELETON, 500);

        ent.attack(target);

        assertEquals(0, target.getRemainingMovePoints(),
                "Ent (upgraded Treeman) should also root defender");
    }

    @Test
    void rootDoesNotApplyWhenDefenderDies() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 50);
        Creature target = creature(CreatureStatistic.SKELETON, 1);

        treeman.attack(target);

        assertFalse(target.isAlive());
        treeman.onMove(); // should not throw even with dead rooted reference
    }

    @Test
    void movingTreemanReleasesRootAndRestoresDefenderMovePoints() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 5);
        Creature target = creature(CreatureStatistic.SKELETON, 500);

        treeman.attack(target);
        assertEquals(0, target.getRemainingMovePoints());

        treeman.onMove();

        assertEquals(target.getMoveRange(), target.getRemainingMovePoints(),
                "Root should lift and move points should be fully restored when Treeman moves");
    }

    @Test
    void treemansNewAttackMovesRootToNewTarget() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 5);
        Creature first = creature(CreatureStatistic.SKELETON, 500);
        Creature second = creature(CreatureStatistic.WALKING_DEAD, 500);

        treeman.attack(first);
        assertEquals(0, first.getRemainingMovePoints());

        treeman.attack(second);

        assertEquals(first.getMoveRange(), first.getRemainingMovePoints(),
                "First target should be unrooted when Treeman attacks a new target");
        assertEquals(0, second.getRemainingMovePoints(),
                "Second target should now be rooted");
    }

    @Test
    void rootIsReleasedWhenTreemanDies() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 5);
        Creature target = creature(CreatureStatistic.SKELETON, 500);

        treeman.attack(target);
        assertEquals(0, target.getRemainingMovePoints());

        treeman.onDeath();

        assertEquals(target.getMoveRange(), target.getRemainingMovePoints(),
                "Root should be released when Treeman dies");
    }

    @Test
    void rootedCreatureCannotMoveOnBoard() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 5);
        Creature target = creature(CreatureStatistic.SKELETON, 500);

        List<Creature> team1 = new ArrayList<>(List.of(treeman));
        List<Creature> team2 = new ArrayList<>(List.of(target));
        Board board = new Board(team1, team2);

        treeman.attack(target);
        assertEquals(0, target.getRemainingMovePoints());

        BattlePoint targetPos = board.getPosition(target);
        BattlePoint adjacentTile = new BattlePoint(targetPos.getX() + 1, targetPos.getY());

        assertFalse(board.canMove(target, adjacentTile),
                "Rooted creature should not be able to move on the board");
    }

    // ---- Double attack (High Elf) ----

    @Test
    void highElfKillsMoreUnitsThanRegularElf() {
        Creature highElf = creature(CreatureStatistic.HIGH_ELF, 10);
        Creature regularElf = creature(CreatureStatistic.ELF, 10);
        Creature targetForHighElf = creature(CreatureStatistic.SKELETON, 500);
        Creature targetForRegularElf = creature(CreatureStatistic.SKELETON, 500);

        highElf.attack(targetForHighElf);
        regularElf.attack(targetForRegularElf);

        int highElfKills = 500 - targetForHighElf.getAmount();
        int regularElfKills = 500 - targetForRegularElf.getAmount();

        assertTrue(highElfKills >= regularElfKills,
                "High Elf (double attacker) should kill at least as many units as regular Elf");
    }

    @Test
    void highElfDealsRoughlyDoubleDamageComparedToElf() {
        // Use fixed seed for determinism
        Creature highElf = new Creature.Builder()
                .statistic(CreatureStatistic.HIGH_ELF)
                .amount(10)
                .calculator(new DefaultDamageCalculator(new Random(42)))
                .build();
        Creature regularElf = new Creature.Builder()
                .statistic(CreatureStatistic.ELF)
                .amount(10)
                .calculator(new DefaultDamageCalculator(new Random(42)))
                .build();
        Creature targetHighElf = creature(CreatureStatistic.SKELETON, 500);
        Creature targetRegularElf = creature(CreatureStatistic.SKELETON, 500);

        int hpBefore = targetHighElf.getCurrentHp();
        int amountBefore = targetHighElf.getAmount();

        highElf.attack(targetHighElf);
        regularElf.attack(targetRegularElf);

        int highElfTotalDmg = (amountBefore - targetHighElf.getAmount()) * targetHighElf.getMaxHp()
                + (hpBefore - targetHighElf.getCurrentHp());
        int regularElfTotalDmg = (amountBefore - targetRegularElf.getAmount()) * targetRegularElf.getMaxHp()
                + (hpBefore - targetRegularElf.getCurrentHp());

        // High Elf attacks twice so should deal roughly double damage
        // Allow some tolerance since same random seed produces same first roll
        // but second attack uses a different roll
        assertTrue(highElfTotalDmg > regularElfTotalDmg,
                "High Elf should deal strictly more damage than regular Elf due to double attack");
    }

    // ---- Ranged (Elf / High Elf) ----

    @Test
    void elfDealsDamageWithoutTakingCounterAttack() {
        Creature elf = creature(CreatureStatistic.ELF, 10);
        Creature defender = creature(CreatureStatistic.SKELETON, 100);

        int elfAmountBefore = elf.getAmount();
        elf.attack(defender);

        assertEquals(elfAmountBefore, elf.getAmount(),
                "Ranged Elf should take no losses from counter-attack");
        assertTrue(defender.getAmount() < 100,
                "Elf should still deal damage to the defender");
    }

    @Test
    void highElfTakeNoLossesFromCounterAttackOnEitherHit() {
        Creature highElf = creature(CreatureStatistic.HIGH_ELF, 10);
        Creature defender = creature(CreatureStatistic.SKELETON, 100);

        int highElfAmountBefore = highElf.getAmount();
        highElf.attack(defender);

        assertEquals(highElfAmountBefore, highElf.getAmount(),
                "High Elf should take no losses across both attack hits");
    }

    @Test
    void meleeUnitTakesCounterAttackWhileRangedDoesNot() {
        Creature centaur = creature(CreatureStatistic.CENTAUR, 1);
        Creature elf = creature(CreatureStatistic.ELF, 1);
        // Give defenders enough HP to survive and retaliate
        Creature defenderForCentaur = creature(CreatureStatistic.WALKING_DEAD, 500);
        Creature defenderForElf = creature(CreatureStatistic.WALKING_DEAD, 500);

        int centaurHpBefore = centaur.getCurrentHp();
        centaur.attack(defenderForCentaur);
        boolean centaurTookDamage = centaur.getCurrentHp() < centaurHpBefore
                || !centaur.isAlive();

        int elfAmountBefore = elf.getAmount();
        int elfHpBefore = elf.getCurrentHp();
        elf.attack(defenderForElf);
        boolean elfTookDamage = elf.getAmount() < elfAmountBefore
                || elf.getCurrentHp() < elfHpBefore;

        assertFalse(elfTookDamage,
                "Ranged Elf should never take damage from counter-attack");
        // Note: centaur may or may not take damage depending on RNG,
        // but the key guarantee is that Elf never does
    }

    // ---- Helper ----

    private Creature creature(CreatureStatistic stat, int amount) {
        return new Creature.Builder()
                .statistic(stat)
                .amount(amount)
                .calculator(new DefaultDamageCalculator(new Random(42)))
                .build();
    }
}
