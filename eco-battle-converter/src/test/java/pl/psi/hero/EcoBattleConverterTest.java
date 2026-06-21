package pl.psi.hero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;

import pl.psi.converter.EcoBattleConverter;
import pl.psi.creatures.Creature;
import pl.psi.creatures.DamageCalculatorIf;
import pl.psi.creatures.EconomyNecropolisFactory;
import pl.psi.creatures.ReducedDamageCalculator;
import pl.psi.hero.skills.impl.ArmorerSkill;
import pl.psi.hero.skills.impl.OffenceSkill;
import pl.psi.map.MapObjectIf;
import pl.psi.map.resources.Resources;

class EcoBattleConverterTest {
    private final Statistics aStats = new Statistics(10, 10, 10, 10);

    @Test
    void shouldConvertCreaturesCorrectly() {
        final EconomyHero ecoHero = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, new Resources(1000, 0, 0, 0, 0, 0, 0), aStats);
        final EconomyNecropolisFactory factory = new EconomyNecropolisFactory();
        ecoHero.addCreature(factory.create(false, 1, 1));
        ecoHero.addCreature(factory.create(false, 2, 2));
        ecoHero.addCreature(factory.create(false, 3, 3));
        ecoHero.addCreature(factory.create(false, 4, 4));
        ecoHero.addCreature(factory.create(false, 5, 5));
        ecoHero.addCreature(factory.create(false, 6, 6));
        ecoHero.addCreature(factory.create(false, 7, 7));

        final List<Creature> convertedCreatures = EcoBattleConverter.convert(ecoHero)
                .getCreatures();

        assertEquals(7, convertedCreatures.size());

        assertEquals("Skeleton", convertedCreatures.get(0)
                .getName());
        assertEquals(1, convertedCreatures.get(0)
                .getAmount());

        assertEquals("Walking Dead", convertedCreatures.get(1)
                .getName());
        assertEquals(2, convertedCreatures.get(1)
                .getAmount());

        assertEquals("Wight", convertedCreatures.get(2)
                .getName());
        assertEquals(3, convertedCreatures.get(2)
                .getAmount());

        assertEquals("Vampire", convertedCreatures.get(3)
                .getName());
        assertEquals(4, convertedCreatures.get(3)
                .getAmount());

        assertEquals("Lich", convertedCreatures.get(4)
                .getName());
        assertEquals(5, convertedCreatures.get(4)
                .getAmount());

        assertEquals("Black Knight", convertedCreatures.get(5)
                .getName());
        assertEquals(6, convertedCreatures.get(5)
                .getAmount());

        assertEquals("Bone Dragon", convertedCreatures.get(6)
                .getName());
        assertEquals(7, convertedCreatures.get(6)
                .getAmount());
    }

    @Test
    void shouldAddHeroAttackAndDefenseToCreatureStatistics() {
        var hero = new EconomyHero(EconomyHero.Fraction.NECROPOLIS,
                new Resources(100, 100, 100, 100, 100, 100, 100),
                new Statistics(3, 4, 1, 1));

        EconomyNecropolisFactory factory = new EconomyNecropolisFactory();
        var ecoCreature = factory.create(false, 1, 10);

        Creature baseCreature = new Creature.Builder()
                .statistic(ecoCreature.getStats())
                .amount(ecoCreature.getAmount())
                .build();

        Creature creatureWithBonuses = EcoBattleConverter.convertCreatureWithEffects(
                ecoCreature, hero
        );

        assertEquals(8, creatureWithBonuses.getAttack());
        assertEquals(8, creatureWithBonuses.getArmor());
    }

    @Test
    void convertedCreatureUsesCombatSkillFactorsFromManager() throws Exception {
        EconomyHero hero = new EconomyHero(
                EconomyHero.Fraction.NECROPOLIS,
                new Resources(0, 0, 0, 0, 0, 0, 0),
                new Statistics(0, 0, 0, 0)
        );
        hero.upgradeSkill(new OffenceSkill());
        hero.upgradeSkill(new ArmorerSkill());

        EconomyNecropolisFactory factory = new EconomyNecropolisFactory();
        var economyCreature = factory.create(false, 1, 1);
        Creature converted = EcoBattleConverter.convertCreatureWithEffects(economyCreature, hero);
        DamageCalculatorIf calculator = converted.getCalculator();

        assertTrue(calculator instanceof ReducedDamageCalculator);
        assertEquals(0.1f, readFactor(calculator, "reduceDamageFactor"), 0.0001f);
        assertEquals(0.1f, readFactor(calculator, "bonusAttackFactor"), 0.0001f);
    }

    private float readFactor(DamageCalculatorIf calculator, String fieldName) throws Exception {
        Field field = ReducedDamageCalculator.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getFloat(calculator);
    }
}
