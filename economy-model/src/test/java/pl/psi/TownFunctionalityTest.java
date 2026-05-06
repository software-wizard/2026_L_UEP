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

public class TownFunctionalityTest {
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
    void recursiveBuildHelperShouldWorkForComplexBuildings() {
        // Capitol requires City Hall -> Town Hall -> Tavern
        // Also requires Castle -> Citadel -> Fort
        town.buildAllPrerequisites(TownBuilding.CAPITOL);
        town.build(TownBuilding.CAPITOL, hero1);

        assertTrue(town.hasBuilt(TownBuilding.CAPITOL));
        assertTrue(town.hasBuilt(TownBuilding.TAVERN));
        assertTrue(town.hasBuilt(TownBuilding.FORT));
    }

    @Test
    void shouldAddResourcesDailyFromCityHallAndSilo() {
        // Setup: Build prerequisites for Resource Silo
        town.build(TownBuilding.MARKETPLACE, hero1);
        town.build(TownBuilding.RESOURCE_SILO, hero1);

        Resources resourcesBefore = new Resources(
                hero1.getResources().getGold(),
                hero1.getResources().getWood(),
                hero1.getResources().getOre(),
                hero1.getResources().getMercury(),
                hero1.getResources().getSulphur(),
                hero1.getResources().getCrystal(),
                hero1.getResources().getGems()
        );

        // Simulation of a turn end
        engine.endOfDay();

        // Village Hall gives 500 Gold, Silo gives +1 Wood and +1 Ore
        assertEquals(resourcesBefore.getGold() + 500, hero1.getResources().getGold());
        assertEquals(resourcesBefore.getWood() + 1, hero1.getResources().getWood());
        assertEquals(resourcesBefore.getOre() + 1, hero1.getResources().getOre());
    }

    @Test
    void townShouldStartWithVillageHallBuilt() {
        Town newTown = new Town(hero1);
        assertTrue(newTown.hasBuilt(TownBuilding.VILLAGE_HALL));
    }

    @Test
    void buildingShouldReturnCorrectCapabilities() {
        assertTrue(TownBuilding.MAGE_GUILD_LVL_1.getProvidedCapabilities()
                .contains(TownCapability.SPELL_PURCHASE));

        assertTrue(TownBuilding.MARKETPLACE.getProvidedCapabilities()
                .contains(TownCapability.RESOURCE_TRADE));

        assertTrue(TownBuilding.VILLAGE_HALL.getProvidedCapabilities().isEmpty());
    }

    @Test
    void townShouldAbsorbCapabilitiesFromBuildings() {
        Town town = new Town(hero1);
        // Assuming hero1 is setup in your @BeforeEach

        assertFalse(town.hasCapability(TownCapability.SPELL_PURCHASE));

        // Build the guild
        town.build(TownBuilding.MAGE_GUILD_LVL_1, hero1);

        // The method should now be resolved and called during buildBuilding()
        assertTrue(town.hasCapability(TownCapability.SPELL_PURCHASE));
    }

    @Test
    void townShouldMaintainCapabilitiesThroughUpgrades() {
        Town town = new Town(hero1);

        town.build(TownBuilding.MAGE_GUILD_LVL_1, hero1);
        town.build(TownBuilding.MAGE_GUILD_LVL_2, hero1);

        assertTrue(town.hasCapability(TownCapability.SPELL_PURCHASE));
    }

    @Test
    void incomeShouldScaleWithHallUpgrades() {
        // Village Hall: 500 gold
        int goldStart = hero1.getResources().getGold();
        town.generateResource();
        assertEquals(500, hero1.getResources().getGold() - goldStart);

        // Upgrade to Town Hall (Prerequisite: Tavern)
        town.build(TownBuilding.TAVERN, hero1);
        town.build(TownBuilding.TOWN_HALL, hero1);

        goldStart = hero1.getResources().getGold();
        town.generateResource();
        assertEquals(1500, hero1.getResources().getGold() - goldStart); // Town Hall gives 1000

        // Upgrade to City Hall
        town.build(TownBuilding.MARKETPLACE, hero1);
        town.build(TownBuilding.BLACKSMITH, hero1);
        town.build(TownBuilding.MAGE_GUILD_LVL_1, hero1);
        town.build(TownBuilding.CITY_HALL, hero1);

        goldStart = hero1.getResources().getGold();
        town.generateResource();
        assertEquals(3500, hero1.getResources().getGold() - goldStart); // City Hall gives 2000
    }

    @Test
    void incomeShouldGoToNewOwnerAfterCapture() {
        // Give Town to Hero 2
        town.setOwner(hero2);

        int hero1Gold = hero1.getResources().getGold();
        int hero2Gold = hero2.getResources().getGold();

        town.generateResource();

        assertEquals(hero1Gold, hero1.getResources().getGold()); // No change
        assertEquals(hero2Gold + 500, hero2.getResources().getGold()); // Village Hall income
    }
}