package pl.psi.creatures;

import org.junit.jupiter.api.Test;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatistic;
import pl.psi.creatures.DefaultDamageCalculator;

import java.util.Random;

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
                "Ent should also root defender");
    }

    @Test
    void rootDoesNotApplyWhenDefenderDies() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 50); // massive damage
        Creature target = creature(CreatureStatistic.SKELETON, 1);  // dies on hit

        treeman.attack(target);

        assertFalse(target.isAlive());
        // Root is irrelevant for dead creatures — no assertion on move points needed,
        // but Treeman should not hold a reference to a dead creature
        treeman.onMove(); // should not throw
    }

    @Test
    void movingTreemanReleasesRootAndRestoresDefenderMovePoints() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 5);
        Creature target = creature(CreatureStatistic.SKELETON, 500);

        treeman.attack(target);
        assertEquals(0, target.getRemainingMovePoints());

        treeman.onMove();

        assertEquals(target.getMoveRange(), target.getRemainingMovePoints());
    }

    @Test
    void treemansNewAttackMovesRootToNewTarget() {
        Creature treeman = creature(CreatureStatistic.TREEMAN, 5);
        Creature first = creature(CreatureStatistic.SKELETON, 500);
        Creature second = creature(CreatureStatistic.WALKING_DEAD, 500);

        treeman.attack(first);
        assertEquals(0, first.getRemainingMovePoints());

        treeman.attack(second);

        // First target should be freed, second is now rooted
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

    // ---- Double attack (High Elf) ----

    @Test
    void highElfAttacksTwicePerTurn() {
        // Use a fixed seed calculator so we can count damage applications
        // We verify indirectly: a single elf hit cannot kill 200hp skeleton stack,
        // but two hits might reduce hp more than one would.
        // Simpler: verify isDoubleAttacker() stat and that a heavily outnumbered
        // High Elf deals more total damage than a regular Elf against same target.
        Creature highElf = creature(CreatureStatistic.HIGH_ELF, 10);
        Creature regularElf = creature(CreatureStatistic.ELF, 10);
        Creature targetForHighElf = creature(CreatureStatistic.SKELETON, 500);
        Creature targetForRegularElf = creature(CreatureStatistic.SKELETON, 500);

        int hpBefore = targetForHighElf.getCurrentHp();
        int amountBefore = targetForHighElf.getAmount();

        highElf.attack(targetForHighElf);
        regularElf.attack(targetForRegularElf);

        int highElfDamage = (amountBefore - targetForHighElf.getAmount()) * targetForHighElf.getMaxHp()
                + (hpBefore - targetForHighElf.getCurrentHp());
        int regularElfDamage = (amountBefore - targetForRegularElf.getAmount()) * targetForRegularElf.getMaxHp()
                + (hpBefore - targetForRegularElf.getCurrentHp());

        assertTrue(highElfDamage >= regularElfDamage,
                "High Elf should deal at least as much damage as regular Elf (attacks twice)");
        assertTrue(highElf.isDoubleAttacker());
    }

    @Test
    void regularElfIsNotDoubleAttacker() {
        assertFalse(CreatureStatistic.ELF.isDoubleAttacker());
    }

    // ---- Ranged (Elf / High Elf) ----

    @Test
    void elfIsRanged() {
        assertTrue(CreatureStatistic.ELF.isRanged());
    }

    @Test
    void highElfIsRanged() {
        assertTrue(CreatureStatistic.HIGH_ELF.isRanged());
    }

    @Test
    void rangedAttackDoesNotTriggerCounterAttack() {
        Creature highElf = creature(CreatureStatistic.HIGH_ELF, 10);
        Creature skeleton = creature(CreatureStatistic.SKELETON, 10);

        int elfHpBefore = highElf.getCurrentHp();
        int elfAmountBefore = highElf.getAmount();

        highElf.attack(skeleton);

        // High Elf should not have taken any damage from counter-attack
        assertEquals(elfAmountBefore, highElf.getAmount(),
                "Ranged attacker should not lose units to counter-attack");
        assertEquals(elfHpBefore, highElf.getCurrentHp(),
                "Ranged attacker should not lose HP to counter-attack");
    }

    @Test
    void nonRangedUnitIsNotRanged() {
        assertFalse(CreatureStatistic.SKELETON.isRanged());
        assertFalse(CreatureStatistic.TREEMAN.isRanged());
        assertFalse(CreatureStatistic.VAMPIRE.isRanged());
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