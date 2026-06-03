package pl.psi.ai;

import com.google.common.collect.HashBiMap;
import org.junit.jupiter.api.Test;
import pl.psi.BattlePoint;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStats;
import com.google.common.collect.Range;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BattleAIAttackTest {

    private Creature createCreature(final String name, final int moveRange, final int maxHp) {
        CreatureStats stats = CreatureStats.builder()
                .name(name)
                .attack(5)
                .armor(0)
                .maxHp(maxHp)
                .moveRange(moveRange)
                .damage(Range.closed(1, 5))
                .tier(1)
                .description("")
                .isUpgraded(false)
                .build();
        return new Creature.Builder().statistic(stats).amount(1).build();
    }

    @Test
    public void ai_attacks_adjacent_enemy_and_kills() {
        Creature enemy = createCreature("Enemy", 0, 1); // low hp so killed in one hit
        Creature aiCreature = createCreature("AI", 3, 10);

        Hero hero1 = new Hero(java.util.List.of(enemy), java.util.List.of());
        Hero hero2 = new Hero(java.util.List.of(aiCreature), java.util.List.of());

        Map<BattlePoint, Creature> bank = new HashMap<>();
        // move the enemy to position adjacent to AI (13,1)
        bank.put(new BattlePoint(13, 1), enemy);

        GameEngine engine = new GameEngine(hero1, hero2, HashBiMap.create(), bank);

        BattleAI ai = new BattleAI();
        new AIController(engine, hero2, ai);

        engine.pass(); // advance to AI turn and let AI act

        // enemy should be gone after attack (killed)
        assertThat(engine.getCreature(new BattlePoint(13, 1))).isEmpty();
    }

    @Test
    public void ai_attacks_and_stays_on_its_tile() {
        Creature enemy = createCreature("Enemy", 0, 1);
        Creature aiCreature = createCreature("AI", 3, 10);

        Hero hero1 = new Hero(java.util.List.of(enemy), java.util.List.of());
        Hero hero2 = new Hero(java.util.List.of(aiCreature), java.util.List.of());

        Map<BattlePoint, Creature> bank = new HashMap<>();
        bank.put(new BattlePoint(13, 1), enemy);

        GameEngine engine = new GameEngine(hero1, hero2, HashBiMap.create(), bank);

        BattleAI ai = new BattleAI();
        new AIController(engine, hero2, ai);

        engine.pass();

        // AI should remain on its starting tile after attacking
        BattlePoint start = new BattlePoint(14, 1);
        assertThat(engine.getCreature(start)).isPresent().contains(aiCreature);
    }

    @Test
    public void ai_does_not_attack_when_enemy_out_of_attack_range_and_moves_instead() {
        Creature enemy = createCreature("Enemy", 0, 10);
        Creature aiCreature = createCreature("AI", 3, 10);

        Hero hero1 = new Hero(java.util.List.of(enemy), java.util.List.of());
        Hero hero2 = new Hero(java.util.List.of(aiCreature), java.util.List.of());

        Map<BattlePoint, Creature> bank = new HashMap<>();
        // place enemy two tiles away (distance == 2) -> should not be attacked
        bank.put(new BattlePoint(12, 1), enemy);

        GameEngine engine = new GameEngine(hero1, hero2, HashBiMap.create(), bank);

        BattleAI ai = new BattleAI();
        new AIController(engine, hero2, ai);

        engine.pass();

        // AI should have moved one step towards enemy (from 14,1 to 13,1)
        BattlePoint expected = new BattlePoint(13, 1);
        assertThat(engine.getCreature(expected)).isPresent().contains(aiCreature);
    }
}

