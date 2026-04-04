package pl.psi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.map.BoardEconomyEngine;
import pl.psi.map.MapObjectIf;
import pl.psi.map.buildings.town.*;
import pl.psi.map.resources.Resources;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreatureShopTest {
    private Town town;
    private EconomyHero hero1;
    private EconomyHero hero2;
    private Resources startingResources;
    private Map<Point, MapObjectIf> map;
    private BoardEconomyEngine engine;

    @BeforeEach
    void init() {
        town = new Town(hero1);
        startingResources = new Resources(100000, 100, 100, 100, 100, 100, 100);
        Statistics aStats = new Statistics(10, 10, 10, 10);
        hero1 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, startingResources, aStats);
        hero2 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, startingResources, aStats);
        town.setOwner(hero1);

        map = new HashMap<>();
        map.put(new Point(1,1), town);
        // Note: Assuming BoardEconomyEngine constructor accepts (hero1, hero2, map)
        engine = new BoardEconomyEngine(hero1, hero2, map);
    }

    @Test
    void buildingNewDwellingShouldGrantInstantBasePopulation() {
        // Building Graveyard should instantly give 8 Walking Dead
        town.build(CreatureBuildings.GRAVEYARD, hero1);
        assertEquals(8, town.getAvailableUnits(CreatureBuildings.GRAVEYARD));
    }

    @Test
    void upgradedDwellingShouldSharePoolWithBase() {
        town.build(CreatureBuildings.GRAVEYARD, hero1);
        town.build(CreatureBuildings.GRAVEYARD_UPGRADED, hero1);

        // Both refer to the same "Base Building" (Graveyard)
        assertEquals(8, town.getAvailableUnits(CreatureBuildings.GRAVEYARD));
        assertEquals(8, town.getAvailableUnits(CreatureBuildings.GRAVEYARD_UPGRADED));

        town.buyUnits(CreatureBuildings.GRAVEYARD_UPGRADED, 3);

        assertEquals(5, town.getAvailableUnits(CreatureBuildings.GRAVEYARD));
    }

    @Test
    void castleBonusShouldDoubleWeeklyGrowthAndFloorValues() {
        town.buildAllPrerequisites(TownBuilding.CASTLE);
        town.build(TownBuilding.CASTLE,hero1);
        town.buildAllPrerequisites(CreatureBuildings.MAUSOLEUM);
        town.build(CreatureBuildings.MAUSOLEUM, hero1); // Base growth 3

        // Initial build gives base only (3)
        assertEquals(3, town.getAvailableUnits(CreatureBuildings.MAUSOLEUM));

        // Generate weekly units (3 * 2.0 = 6)
        town.generateUnits();
        assertEquals(9, town.getAvailableUnits(CreatureBuildings.MAUSOLEUM));
    }

    @Test
    void citadelBonusShouldApplyFiftyPercentAndRoundDown() {
        town.buildAllPrerequisites(TownBuilding.CITADEL);
        town.build(TownBuilding.CITADEL,hero1); // 1.5x modifier
        town.buildAllPrerequisites(CreatureBuildings.TOMB_OF_SOULS);
        town.build(CreatureBuildings.TOMB_OF_SOULS, hero1); // Base 7

        // 7 * 1.5 = 10.5 -> Should round down to 10
        town.generateUnits();

        // 7 (initial) + 10 (growth) = 17
        assertEquals(17, town.getAvailableUnits(CreatureBuildings.TOMB_OF_SOULS));
    }

    @Test
    void populationShouldAccumulateOverMultipleDays() {
        town.build(CreatureBuildings.GRAVEYARD, hero1); // Initial: 8

        // Simulate 2 generate cycles (e.g., 2 weeks in engine)
        town.generateUnits();
        town.generateUnits();

        // 8 (start) + 8 (week 2) + 8 (week 3) = 24
        assertEquals(24, town.getAvailableUnits(CreatureBuildings.GRAVEYARD));
    }

    @Test
    void buildingMageGuildLevelTwoShouldProvideDifferentCapabilities() {
        // Test that upgrading doesn't remove the base capability
        town.build(TownBuilding.MAGE_GUILD_LVL_1, hero1);
        assertTrue(town.hasCapability(TownCapability.SPELL_PURCHASE));

        town.build(TownBuilding.MAGE_GUILD_LVL_2, hero1);
        assertTrue(town.hasCapability(TownCapability.SPELL_PURCHASE)); // Still exists
    }
}
