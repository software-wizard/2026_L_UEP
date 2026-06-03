package pl.psi.ai;

import pl.psi.BattlePoint;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.creatures.Creature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Very small, self-contained greedy movement AI.
 * It chooses a target point on the straight-line path toward the nearest enemy
 * limited by creature move range and avoiding occupied tiles.
 */
public class BattleAI {

    /**
     * Choose greedy move target.
     * @param start current position of the unit
     * @param moveRange maximum number of steps the unit can take
     * @param enemies list of enemy positions (must not be empty to return a move)
     * @param occupied set of positions considered blocked
     * @return optional MoveAction with chosen target or empty when no move possible
     */
    public Optional<MoveAction> chooseGreedyMove(final BattlePoint start,
                                                 final int moveRange,
                                                 final List<BattlePoint> enemies,
                                                 final Set<BattlePoint> occupied) {

        if (enemies == null || enemies.isEmpty()) {
            return Optional.empty();
        }

        // find nearest enemy by Euclidean distance
        BattlePoint nearest = null;
        double minDist = Double.MAX_VALUE;
        for (BattlePoint e : enemies) {
            double d = start.distance(e);
            if (d < minDist) {
                minDist = d;
                nearest = e;
            }
        }

        if (nearest == null) {
            return Optional.empty();
        }

        int dx = Integer.signum(nearest.getX() - start.getX());
        int dy = Integer.signum(nearest.getY() - start.getY());

        int x = start.getX();
        int y = start.getY();

        BattlePoint lastFree = null;

        for (int step = 1; step <= moveRange; step++) {
            if (x != nearest.getX()) x += dx;
            if (y != nearest.getY()) y += dy;

            BattlePoint point = new BattlePoint(x, y);

            if (occupied != null && occupied.contains(point)) {
                // path is blocked here; cannot step onto occupied tile -> stop
                break;
            } else {
                lastFree = point;
            }

            if (x == nearest.getX() && y == nearest.getY()) {
                break;
            }
        }

        if (lastFree == null) {
            return Optional.empty();
        }

        return Optional.of(new MoveAction(lastFree));
    }

    /**
     * Higher-level decision method that uses GameEngine's public API to inspect the board
     * and returns either MoveAction, AttackAction, PassAction or empty when nothing to do.
     */
    public Optional<Action> decide(final BattlePoint currentPos,
                                    final Creature creature,
                                    final GameEngine engine,
                                    final Hero aiHero) {

        // scan board for enemies and occupied tiles (uses public GameEngine API)
        List<BattlePoint> enemies = new ArrayList<>();
        Set<BattlePoint> occupied = new HashSet<>();

        for (int x = 0; x <= 14; x++) {
            for (int y = 0; y <= 14; y++) {
                BattlePoint p = new BattlePoint(x, y);
                engine.getCreature(p).ifPresent(c -> {
                    occupied.add(p);
                    if (!aiHero.getCreatures().contains(c)) {
                        enemies.add(p);
                    }
                });
            }
        }

        if (enemies.isEmpty()) {
            return Optional.empty();
        }

        // if any enemy is in attack range, attack the first one reachable
        for (BattlePoint e : enemies) {
            if (engine.canAttack(e)) {
                return Optional.of(new AttackAction(e));
            }
        }

        // otherwise compute greedy move towards nearest enemy
        Optional<MoveAction> move = chooseGreedyMove(currentPos, creature.getMoveRange(), enemies, occupied);
        if (move.isPresent()) {
            return Optional.of(move.get());
        }

        return Optional.empty();
    }
}

