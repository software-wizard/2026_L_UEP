package pl.psi.ai;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;
import pl.psi.BattlePoint;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStats;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BattleAIEngineTest {

    private Creature createCreature(final String name, final int moveRange) {
        CreatureStats stats = CreatureStats.builder()
                .name(name)
                .attack(1)
                .armor(0)
                .maxHp(10)
                .moveRange(moveRange)
                .damage(Range.closed(1, 1))
                .tier(1)
                .description("")
                .isUpgraded(false)
                .build();
        return new Creature.Builder().statistic(stats).amount(1).build();
    }

    @Test
    public void ai_moves_towards_enemy() {
        Creature enemy = createCreature("Enemy", 0);
        Creature aiCreature = createCreature("AI", 3);

        Hero hero1 = new Hero(java.util.List.of(enemy), java.util.List.of());
        Hero hero2 = new Hero(java.util.List.of(aiCreature), java.util.List.of());

        GameEngine engine = new GameEngine(hero1, hero2);

        BattleAI ai = new BattleAI();
        new AIController(engine, hero2, ai);

        engine.pass(); // advance to AI turn

        BattlePoint expected = new BattlePoint(11, 1);
        assertThat(engine.getCreature(expected)).isPresent().contains(aiCreature);
    }

    @Test
    public void ai_stops_before_blocker() {
        Creature enemy = createCreature("Enemy", 0);
        Creature aiCreature = createCreature("AI", 3);
        Creature blocker = createCreature("Blocker", 1);

        Hero hero1 = new Hero(java.util.List.of(enemy), java.util.List.of());
        Hero hero2 = new Hero(java.util.List.of(aiCreature), java.util.List.of());

        Map<BattlePoint, Creature> bank = new HashMap<>();
        bank.put(new BattlePoint(11, 1), blocker);

        GameEngine engine = new GameEngine(hero1, hero2, HashBiMap.create(), bank);

        BattleAI ai = new BattleAI();
        new AIController(engine, hero2, ai);

        engine.pass();

        BattlePoint expected = new BattlePoint(12, 1);
        assertThat(engine.getCreature(expected)).isPresent().contains(aiCreature);
    }

    @Test
    public void ai_does_not_move_if_path_fully_blocked() {
        Creature enemy = createCreature("Enemy", 0);
        Creature aiCreature = createCreature("AI", 3);
        Creature b1 = createCreature("B1", 1);
        Creature b2 = createCreature("B2", 1);
        Creature b3 = createCreature("B3", 1);

        Hero hero1 = new Hero(java.util.List.of(enemy), java.util.List.of());
        Hero hero2 = new Hero(java.util.List.of(aiCreature), java.util.List.of());

        Map<BattlePoint, Creature> bank = new HashMap<>();
        bank.put(new BattlePoint(13, 1), b1);
        bank.put(new BattlePoint(12, 1), b2);
        bank.put(new BattlePoint(11, 1), b3);

        GameEngine engine = new GameEngine(hero1, hero2, HashBiMap.create(), bank);

        BattleAI ai = new BattleAI();
        new AIController(engine, hero2, ai);

        engine.pass();

        BattlePoint start = new BattlePoint(14, 1);
        assertThat(engine.getCreature(start)).isPresent().contains(aiCreature);
    }
}

