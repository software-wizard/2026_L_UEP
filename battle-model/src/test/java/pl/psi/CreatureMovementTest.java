package pl.psi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStatistic;
import pl.psi.creatures.DefaultDamageCalculator;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class CreatureMovementTest {

    private Creature walker;

    @BeforeEach
    void setUp() {
        walker = new Creature.Builder()
                .statistic(CreatureStatistic.SKELETON)
                .amount(10)
                .calculator(new DefaultDamageCalculator(new Random(0)))
                .build();
    }

    @Test
    void creatureCannotMoveAfterMovingFullRange() {
        // After consuming all move points the creature should have 0 remaining
        walker.reduceMovePoints(walker.getMoveRange());
        assertEquals(0, walker.getRemainingMovePoints());
    }

    @Test
    void movePointsResetAtEndOfTurn() {
        walker.reduceMovePoints(walker.getMoveRange()); // exhaust all points

        walker.propertyChange(
                new java.beans.PropertyChangeEvent(this, TurnQueue.END_OF_TURN, 0, 1));

        assertEquals(walker.getMoveRange(), walker.getRemainingMovePoints(),
                "Move points should be fully restored at end of turn");
    }
}