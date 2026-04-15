package pl.psi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.map.buildings.town.BuildingType;
import pl.psi.map.buildings.town.Town;
import pl.psi.map.buildings.town.TownBuilding;
import pl.psi.map.buildings.town.CreatureBuildings;
import pl.psi.map.resources.Resources;

import static org.junit.jupiter.api.Assertions.*;

public class TownUpgradeTest {

    private Town town;
    private EconomyHero hero;
    private Resources resources;

    @BeforeEach
    void init() {
        town = new Town(hero);
        resources = new Resources(100000,1000,1000,1000,1000,1000,1000);
        Statistics aStats = new Statistics(10, 10, 10, 10);
        hero = new EconomyHero(EconomyHero.Fraction.NECROPOLIS,resources, aStats);

    }

    @Test
    void shouldOnlyUnlockBaseCreatureAfterBuildingBaseBuilding() {
        // GIVEN
        CreatureBuildings baseBuilding = CreatureBuildings.CURSED_TEMPLE;

        // THEN
        assertTrue(town.hasBuilt(baseBuilding));
        assertFalse(town.hasBuilt(CreatureBuildings.CURSED_TEMPLE_UPGRADED));
    }

    @Test
    void shouldUnlockUpgradedCreatureAfterBuildingUpgrade() {
        // GIVEN
        CreatureBuildings upgraded = CreatureBuildings.CURSED_TEMPLE_UPGRADED;

        town.build(upgraded, hero);

        // THEN
        assertTrue(town.hasBuilt(upgraded));
    }

    @Test
    void shouldSubtractResourcesFromHeroWhenBuilding() {
        // GIVEN
        CreatureBuildings building = CreatureBuildings.CURSED_TEMPLE_UPGRADED;
        town.buildAllPrerequisites(building);
        int initialGold = hero.getResources().getGold();

        // WHEN
        town.build(building, hero);

        // THEN
        int expectedGold = initialGold - building.getCost().getGold();
        assertEquals(expectedGold, hero.getResources().getGold());
    }

    @Test
    void shouldNotAllowBuildingSameUpgradeTwice() {
        // GIVEN
        CreatureBuildings building = CreatureBuildings.CURSED_TEMPLE;

        // THEN
        assertThrows(IllegalStateException.class, () -> town.build(building, hero));
    }

    @Test
    void shouldAllowUpgradeAfterBuildingBase() {
        // GIVEN
        CreatureBuildings base = CreatureBuildings.CURSED_TEMPLE;
        CreatureBuildings upgrade = CreatureBuildings.CURSED_TEMPLE_UPGRADED;
        town.buildAllPrerequisites(base);

        town.build(upgrade, hero);

        // THEN
        assertTrue(town.hasBuilt(base));
        assertTrue(town.hasBuilt(upgrade));
    }

    @Test
    void shouldBuildTownHallThenCityHallThenCapitol() {

        town.setOwner(hero);
        town.buildAllPrerequisites(TownBuilding.CAPITOL);
        town.build(TownBuilding.CAPITOL, hero);

        assertTrue(town.hasBuilt(TownBuilding.CAPITOL));
    }

    @Test
    void townShouldStartWithCorrectNecropolisDefaults() {
        // H3 defaults: Village Hall, Fort, and Tier 1 Dwelling
        assertTrue(town.hasBuilt(TownBuilding.VILLAGE_HALL));
        assertTrue(town.hasBuilt(TownBuilding.FORT));
        assertTrue(town.hasBuilt(CreatureBuildings.CURSED_TEMPLE));// Initial Village Hall income
    }

    @Test
    void shouldThrowExceptionWhenBuildingDuplicate() {
        assertThrows(IllegalStateException.class, () -> town.build(TownBuilding.FORT, hero));
    }

    @Test
    void shouldRespectComplexPrerequisites() {
        // Capitol requires City Hall and Castle
        assertThrows(IllegalStateException.class, () -> town.build(TownBuilding.CAPITOL, hero));

        // Build chain for Castle
        town.build(TownBuilding.CITADEL, hero);
        town.build(TownBuilding.CASTLE, hero);

        // Build chain for City Hall (needs Tavern, Marketplace, Blacksmith, Mage Guild)
        town.build(TownBuilding.TAVERN, hero);
        town.build(TownBuilding.MARKETPLACE, hero);
        town.build(TownBuilding.BLACKSMITH, hero);
        town.build(TownBuilding.MAGE_GUILD_LVL_1, hero);
        town.build(TownBuilding.TOWN_HALL, hero);
        town.build(TownBuilding.CITY_HALL, hero);

        assertDoesNotThrow(() -> town.build(TownBuilding.CAPITOL, hero));
    }

    @Test
    void shouldNotBuildIfHeroCannotAfford() {
        // Setup a hero with zero gold
        Resources poorResources = new Resources(0, 0, 0, 0, 0, 0, 0);
        EconomyHero poorHero = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, poorResources, new Statistics(1,1,1,1));

        // Attempting to build a Blacksmith (requires 1000 gold)
        assertThrows(IllegalStateException.class, () -> town.build(TownBuilding.BLACKSMITH, poorHero));

        // Verify town state didn't change
        assertFalse(town.hasBuilt(TownBuilding.BLACKSMITH));
    }

    @Test
    void shouldNotBeAbleToBuildSecondBuildingInTurn(){
        town.build(TownBuilding.TAVERN, hero);
        assertThrows(IllegalStateException.class, () -> town.build(TownBuilding.MAGE_GUILD_LVL_1, hero));
    }
}
