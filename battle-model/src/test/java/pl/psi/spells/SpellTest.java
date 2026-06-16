package pl.psi.spells;

import com.google.common.collect.Range;
import org.junit.jupiter.api.Test;
import pl.psi.BattlePoint;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.Spells.BuffSpell;
import pl.psi.Spells.DamageSpell;
import pl.psi.Spells.DebuffSpell;
import pl.psi.Spells.Spell;
import pl.psi.TurnQueue;
import pl.psi.creatures.Creature;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpellTest {

    @Test
    void damagingSpellShouldApplyDamage(){
        final Creature c1 = createDummyCreature();
        final Hero h1 = new Hero(List.of(c1), List.of());
        final Spell deafultDamageSpell = new DamageSpell("deafult", 1, 1);

        h1.castSpell(deafultDamageSpell, c1);

        assertThat(c1.getCurrentHp()).isBetween(70, 95);
    }

    @Test
    void buffSpellShouldAddStats(){
        final Creature c1 = createDummyCreature();
        final Hero h1 = new Hero(List.of(c1), List.of());
        pl.psi.creatures.CreatureStats statBuff = pl.psi.creatures.CreatureStats.builder()
                .attack(5)
                .build();

        Spell buffSpell = new BuffSpell("", 1,1,  statBuff);
        h1.castSpell(buffSpell, c1);

        assertThat(c1.getAttack()).isEqualTo(15);
    }

    @Test
    void debuffSpellShouldSubtractStats(){
        final Creature c1 = createDummyCreature();
        final Hero h1 = new Hero(List.of(c1), List.of());
        pl.psi.creatures.CreatureStats statDebuff = pl.psi.creatures.CreatureStats.builder()
                .attack(5)
                .build();

        Spell debuffspell = new DebuffSpell("", 1, 1, statDebuff);
        h1.castSpell(debuffspell, c1);

        assertThat(c1.getAttack()).isEqualTo(5);
    }

    @Test
    void statsShouldComeBackToOriginalAfterNturnsBasedOnSpellPower(){
        final Creature attacker = createDummyCreature();
        final Creature defender = createDummyCreature();
        final Hero h1 = new Hero(List.of(attacker), List.of());
        h1.setSpellPower(1); // effective duration = 1 (duration) + 1 (power) = 2

        pl.psi.creatures.CreatureStats statBuff = pl.psi.creatures.CreatureStats.builder()
                .attack(5)
                .build();

        Spell buffSpell = new BuffSpell("", 1, 1, statBuff);
        final TurnQueue turnQueue = new TurnQueue(List.of(attacker), List.of(defender));

        h1.castSpell(buffSpell, attacker);
        assertThat(attacker.getAttack()).isEqualTo(15);

        turnQueue.next(); // defender's turn
        turnQueue.next(); // attacker's turn (EOT 1) - duration becomes 1
        assertThat(attacker.getAttack()).isEqualTo(15);

        turnQueue.next(); // defender's turn
        turnQueue.next(); // attacker's turn (EOT 2) - duration becomes 0 -> expires
        assertThat(attacker.getAttack()).isEqualTo(10);
    }

    private Creature createDummyCreature() {
        return new Creature.Builder()
                .statistic(pl.psi.creatures.CreatureStats.builder()
                        .maxHp(100)
                        .damage(Range.closed(5, 10))
                        .attack(10)
                        .armor(5)
                        .build())
                .build();
    }

    @Test
    void spellCastingDurationIsAffectedByHeroSpellPower() {
        Hero hero = new Hero(List.of(createDummyCreature()), List.of());
        hero.setSpellPower(2); // effective duration = 1 (base) + 2 (power) = 3
        Creature target = createDummyCreature();
        TurnQueue turnQueue = new TurnQueue(List.of(target), List.of());
        
        Spell buff = new BuffSpell("Bless", 1, 1, pl.psi.creatures.CreatureStats.builder().attack(5).build());
        
        hero.castSpell(buff, target);
        
        assertThat(target.getAttack()).isEqualTo(15);
        turnQueue.next(); // round 1 (EOT 1) -> duration = 2
        assertThat(target.getAttack()).isEqualTo(15);
        turnQueue.next(); // round 2 (EOT 2) -> duration = 1
        assertThat(target.getAttack()).isEqualTo(15);
        turnQueue.next(); // round 3 (EOT 3) -> duration = 0 -> expires
        assertThat(target.getAttack()).isEqualTo(10);
    }

    @Test
    void heroCanOnlyCastOneSpellPerRound() {
        Hero hero = new Hero(List.of(createDummyCreature()), List.of());
        Creature target = createDummyCreature();
        Spell dmg = new DamageSpell("Zap", 1, 1);

        hero.castSpell(dmg, target);
        assertThat(hero.getSpellCastingState().canCast()).isFalse();

        assertThrows(IllegalStateException.class, () -> hero.castSpell(dmg, target));
    }

    @Test
    void heroCanHaveMoreThanOneBuffSpellWithDifferentDurations(){
        final Creature c1 = createDummyCreature();
        final Hero h1 = new Hero(List.of(c1), List.of());

        pl.psi.creatures.CreatureStats statBuff = pl.psi.creatures.CreatureStats.builder()
                .attack(5)
                .build();

        // Effective duration = base + power
        // buff1: 1 + 1 = 2 rounds
        // buff2: 2 + 1 = 3 rounds
        Spell buffSpellOne = new BuffSpell("", 1, 1,  statBuff);
        Spell buffSpellTwo = new BuffSpell("", 1, 2,  statBuff);
        final TurnQueue turnQueue = new TurnQueue(List.of(c1), List.of());

        h1.setSpellPower(1);
        h1.castSpell(buffSpellOne, c1);
        
        // Reset cast state for next round manual trigger
        h1.propertyChange(new java.beans.PropertyChangeEvent(this, TurnQueue.END_OF_TURN, 0, 1));
        h1.castSpell(buffSpellTwo, c1);

        assertThat(c1.getAttack()).isEqualTo(20);

        turnQueue.next(); // Round 1 (EOT 1) -> buff1 becomes 1, buff2 becomes 2
        assertThat(c1.getAttack()).isEqualTo(20);

        turnQueue.next(); // Round 2 (EOT 2) -> buff1 becomes 0 (expires), buff2 becomes 1
        assertThat(c1.getAttack()).isEqualTo(15);

        turnQueue.next(); // Round 3 (EOT 3) -> buff2 becomes 0 (expires)
        assertThat(c1.getAttack()).isEqualTo(10);
    }

    @Test
    void fireballShouldDealScaledDamageToAllCreaturesInRadius() {
        // Given - hero1 has a creature (so TurnQueue includes it), hero2 has targets
        Creature attacker = createDummyCreature();
        Creature target1 = createDummyCreature();
        Creature target2 = createDummyCreature();

        Hero hero1 = new Hero(List.of(attacker), List.of());
        Hero hero2 = new Hero(List.of(target1, target2), List.of());

        GameEngine engine = new GameEngine(hero1, hero2);

        int hpBefore1 = target1.getCurrentHp();
        int hpBefore2 = target2.getCurrentHp();

        // Base damage for lvl 2 is 20. SpellPower is 1 by default.
        // Formula: (20 + 1 * 10) = 30 damage. Reduced by 10% armor (5 armor) = 27 damage.
        DamageSpell fireball = new DamageSpell("Fireball", 2, 0, new pl.psi.Spells.RadiusArea(2.5));

        // When - hero1's creature is current, so getCurrentHero() returns hero1
        engine.castSpell(fireball, new BattlePoint(14, 2));

        // Then
        assertThat(target1.getCurrentHp()).isEqualTo(hpBefore1 - 27);
        assertThat(target2.getCurrentHp()).isEqualTo(hpBefore2 - 27);
    }
}
