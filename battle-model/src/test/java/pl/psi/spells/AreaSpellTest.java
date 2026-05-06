package pl.psi.spells;

import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;
import pl.psi.BattlePoint;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.Spells.DamageSpell;
import pl.psi.Spells.RadiusArea;
import pl.psi.Spells.SingleTargetArea;
import pl.psi.Spells.Spell;
import pl.psi.creatures.Creature;
import pl.psi.creatures.CreatureStats;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AreaSpellTest {

    private Creature createDummyCreature() {
        return new Creature.Builder()
                .statistic(CreatureStats.builder()
                        .maxHp(100)
                        .damage(Range.closed(5, 10))
                        .attack(10)
                        .armor(5)
                        .build())
                .build();
    }

    @Test
    void singleTargetAreaShouldOnlyHitOneCreature() {
        Creature target = createDummyCreature();
        Creature bystander = createDummyCreature();

        Hero hero1 = new Hero(List.of(target, bystander), List.of());
        Hero hero2 = new Hero(List.of(), List.of());

        GameEngine engine = new GameEngine(hero1, hero2);
        
        // Ensure creatures are on the board at specific spots
        // In GameEngine, Hero 1 creatures are added at x=0. 
        // target is at (0, 1), bystander is at (0, 3).
        
        Spell singleTargetDamage = new DamageSpell("Magic Arrow", 1, 1, new SingleTargetArea());
        
        // Cast at (0, 1)
        engine.castSpell(singleTargetDamage, new BattlePoint(0, 1));

        // Target should be damaged (currentHp < 100)
        assertThat(target.getCurrentHp()).isLessThan(100);
        
        // Bystander should not be damaged (currentHp == 100)
        assertThat(bystander.getCurrentHp()).isEqualTo(100);
    }

    @Test
    void radiusAreaShouldHitMultipleCreatures() {
        Creature c1 = createDummyCreature(); // (0, 1)
        Creature c2 = createDummyCreature(); // (0, 3)
        Creature c3 = createDummyCreature(); // (0, 5)

        Hero hero1 = new Hero(List.of(c1, c2, c3), List.of());
        Hero hero2 = new Hero(List.of(), List.of());

        GameEngine engine = new GameEngine(hero1, hero2);
        
        // Radius of 2 from (0,3). Distance to (0,1) is 2. Distance to (0,5) is 2.
        // So a spell cast at (0,3) with radius 2 should hit all three.
        Spell fireball = new DamageSpell("Fireball", 1, 1, new RadiusArea(2.0));
        
        // Cast at center point (0, 3) where c2 is standing
        engine.castSpell(fireball, new BattlePoint(0, 3));

        // All three creatures should be damaged
        assertThat(c1.getCurrentHp()).isLessThan(100);
        assertThat(c2.getCurrentHp()).isLessThan(100);
        assertThat(c3.getCurrentHp()).isLessThan(100);
    }

    @Test
    void shouldHitMultipleCreaturesWhenCastOnEmptyHex() {
        Creature c1 = createDummyCreature(); // (0, 1)
        Creature c2 = createDummyCreature(); // (0, 3)

        Hero hero1 = new Hero(List.of(c1, c2), List.of());
        Hero hero2 = new Hero(List.of(), List.of());

        GameEngine engine = new GameEngine(hero1, hero2);

        // Spell with radius 1. Cast at (0, 2) which is EMPTY!
        // Distance to (0, 1) is 1. Distance to (0, 3) is 1.
        // Both should be hit.
        Spell splashDamage = new DamageSpell("Splash", 1, 1, new RadiusArea(1.0));

        // Act: Engine casts spell on an empty point (0, 2) just like the UI would now do
        engine.castSpell(splashDamage, new BattlePoint(0, 2));

        // Assert: Both creatures took damage from an empty hex explosion
        assertThat(c1.getCurrentHp()).isLessThan(100);
        assertThat(c2.getCurrentHp()).isLessThan(100);
    }
}
