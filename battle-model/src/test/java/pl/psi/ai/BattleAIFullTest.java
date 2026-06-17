package pl.psi.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.psi.BattlePoint;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.creatures.Creature;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class BattleAIFullTest {
    private BattleAI battleAI;

    @BeforeEach
    void setUp() {
        battleAI = new BattleAI();
    }

    @Test
    void shouldReturnEmptyWhenNoEnemies() {
        // Objaśnienie: Test sprawdzający przypadek braku wrogów - AI powinno zwrócić Optional.empty().
        BattlePoint start = new BattlePoint(0, 0);
        Optional<MoveAction> move = battleAI.chooseGreedyMove(start, 5, Collections.emptyList(), new HashSet<>());

        assertTrue(move.isEmpty(), "AI nie powinno wykonywać ruchu, gdy nie ma wrogów.");
    }

    @Test
    void shouldMoveTowardsNearestEnemy() {
        // Objaśnienie: Test sprawdza zachłanny ruch AI w prostej linii bez przeszkód.
        BattlePoint start = new BattlePoint(0, 0);
        List<BattlePoint> enemies = List.of(new BattlePoint(5, 0));
        Set<BattlePoint> occupied = new HashSet<>();

        Optional<MoveAction> move = battleAI.chooseGreedyMove(start, 3, enemies, occupied);

        assertTrue(move.isPresent());
        // Z zasięgiem 3, AI zaczynając od (0,0) idąc do (5,0) powinno wylądować na (3,0)
        assertEquals(new BattlePoint(3, 0), move.get().getTarget(), "AI powinno wykorzystać cały ruch na dojście do wroga");
    }

    @Test
    void shouldStopBeforeObstacle() {
        // Objaśnienie: AI powinno zatrzymać się, gdy na drodze stoi zablokowane pole (occupied).
        BattlePoint start = new BattlePoint(0, 0);
        List<BattlePoint> enemies = List.of(new BattlePoint(5, 0));
        Set<BattlePoint> occupied = Set.of(new BattlePoint(2, 0)); // Przeszkoda na drodze!

        Optional<MoveAction> move = battleAI.chooseGreedyMove(start, 4, enemies, occupied);

        assertTrue(move.isPresent());
        // Choć ma 4 punkty ruchu, zablokowane pole (2,0) zatrzyma je na (1,0)
        assertEquals(new BattlePoint(1, 0), move.get().getTarget(), "AI powinno zatrzymać się przed zajętym polem.");
    }
}
