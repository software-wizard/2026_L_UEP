package pl.psi.ai;

import org.junit.jupiter.api.Test;

import pl.psi.BattlePoint;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BattleAITest {

    @Test
    void movesTowardsNearestEnemyWithinRange() {
        BattleAI ai = new BattleAI();
        BattlePoint start = new BattlePoint(0, 0);
        List<BattlePoint> enemies = List.of(new BattlePoint(5, 0));

        Optional<MoveAction> action = ai.chooseGreedyMove(start, 3, enemies, Set.of());

        assertTrue(action.isPresent());
        assertEquals(new BattlePoint(3, 0), action.get().getTarget());
    }

    @Test
    void avoidsOccupiedTilesAndStopsBeforeBlock() {
        BattleAI ai = new BattleAI();
        BattlePoint start = new BattlePoint(0, 0);
        List<BattlePoint> enemies = List.of(new BattlePoint(5, 0));

        // tile at (3,0) is occupied, so AI should stop at (2,0)
        Set<BattlePoint> occupied = Set.of(new BattlePoint(3, 0));

        Optional<MoveAction> action = ai.chooseGreedyMove(start, 3, enemies, occupied);

        assertTrue(action.isPresent());
        assertEquals(new BattlePoint(2, 0), action.get().getTarget());
    }

    @Test
    void returnsEmptyWhenAllPathBlocked() {
        BattleAI ai = new BattleAI();
        BattlePoint start = new BattlePoint(0, 0);
        List<BattlePoint> enemies = List.of(new BattlePoint(5, 0));

        Set<BattlePoint> occupied = Set.of(new BattlePoint(1, 0), new BattlePoint(2, 0), new BattlePoint(3, 0));

        Optional<MoveAction> action = ai.chooseGreedyMove(start, 3, enemies, occupied);

        assertTrue(action.isEmpty());
    }
}

