package pl.psi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.map.buildings.town.BastionUpgradeBuildings;
import pl.psi.map.buildings.town.BuildingType;
import pl.psi.map.buildings.town.Town;
import pl.psi.map.resources.Resources;

import static org.junit.jupiter.api.Assertions.*;

public class BastionTownUpgradeTest {

    private Town town;
    private EconomyHero hero;

    @BeforeEach
    void init() {
        town = new Town();
        Statistics aStats = new Statistics(10, 10, 10, 10);
        hero = new EconomyHero(
                EconomyHero.Fraction.BASTION,
                new Resources(100000, 1000, 1000, 1000, 1000, 1000, 1000),
                aStats);
    }

    @Test
    void shouldOnlyUnlockBaseCreatureAfterBuildingBaseDwelling() {
        buildAllPrerequisites(BastionUpgradeBuildings.CENTAUR_STABLES);
        town.build(BastionUpgradeBuildings.CENTAUR_STABLES, hero);

        assertTrue(town.hasBuilt(BastionUpgradeBuildings.CENTAUR_STABLES));
        assertFalse(town.hasBuilt(BastionUpgradeBuildings.CENTAUR_STABLES_UPGRADED));
    }

    @Test
    void shouldUnlockUpgradedCreatureAfterBuildingUpgradedDwelling() {
        buildAllPrerequisites(BastionUpgradeBuildings.CENTAUR_STABLES_UPGRADED);
        town.build(BastionUpgradeBuildings.CENTAUR_STABLES, hero);
        town.build(BastionUpgradeBuildings.CENTAUR_STABLES_UPGRADED, hero);

        assertTrue(town.hasBuilt(BastionUpgradeBuildings.CENTAUR_STABLES_UPGRADED));
    }

    @Test
    void shouldNotAllowBuildingWithoutPrerequisites() {
        assertThrows(IllegalStateException.class,
                () -> town.build(BastionUpgradeBuildings.DWARF_COTTAGE, hero),
                "Should not be able to build DWARF_COTTAGE without CENTAUR_STABLES");
    }

    @Test
    void shouldNotAllowBuildingSameBuildingTwice() {
        buildAllPrerequisites(BastionUpgradeBuildings.CENTAUR_STABLES);
        town.build(BastionUpgradeBuildings.CENTAUR_STABLES, hero);

        assertThrows(IllegalStateException.class,
                () -> town.build(BastionUpgradeBuildings.CENTAUR_STABLES, hero));
    }

    @Test
    void shouldSubtractResourcesFromHeroWhenBuilding() {
        buildAllPrerequisites(BastionUpgradeBuildings.CENTAUR_STABLES);
        int initialGold = hero.getResources().getGold();

        town.build(BastionUpgradeBuildings.CENTAUR_STABLES, hero);

        int expectedGold = initialGold - BastionUpgradeBuildings.CENTAUR_STABLES.getCost().getGold();
        assertEquals(expectedGold, hero.getResources().getGold());
    }

    @Test
    void shouldBuildFullDwarfChain() {
        buildAllPrerequisites(BastionUpgradeBuildings.DWARF_COTTAGE);
        town.build(BastionUpgradeBuildings.DWARF_COTTAGE, hero);
        town.build(BastionUpgradeBuildings.DWARF_COTTAGE_UPGRADED, hero);

        assertTrue(town.hasBuilt(BastionUpgradeBuildings.DWARF_COTTAGE));
        assertTrue(town.hasBuilt(BastionUpgradeBuildings.DWARF_COTTAGE_UPGRADED));
    }

    @Test
    void shouldBuildFullElfChain() {
        buildAllPrerequisites(BastionUpgradeBuildings.HOMESTEAD);
        town.build(BastionUpgradeBuildings.HOMESTEAD, hero);
        town.build(BastionUpgradeBuildings.HOMESTEAD_UPGRADED, hero);

        assertTrue(town.hasBuilt(BastionUpgradeBuildings.HOMESTEAD));
        assertTrue(town.hasBuilt(BastionUpgradeBuildings.HOMESTEAD_UPGRADED));
    }

    @Test
    void bastionAndNecropolisBuildingsAreIndependent() {
        buildAllPrerequisites(BastionUpgradeBuildings.CENTAUR_STABLES);
        town.build(BastionUpgradeBuildings.CENTAUR_STABLES, hero);

        assertTrue(town.hasBuilt(BastionUpgradeBuildings.CENTAUR_STABLES));
        // Building Bastion dwelling should not affect Necropolis buildings
        assertFalse(town.hasBuilt(
                pl.psi.map.buildings.town.UpgradeBuildings.CURSED_TEMPLE),
                "Bastion buildings should not affect Necropolis buildings");
    }

    private void buildAllPrerequisites(BuildingType building) {
        for (BuildingType prereq : building.getPrerequisites()) {
            if (!town.hasBuilt(prereq)) {
                buildAllPrerequisites(prereq);
                town.build(prereq, hero);
            }
        }
    }
}
