package pl.psi.map.buildings.town;

import pl.psi.creatures.CreatureStatistic;
import pl.psi.hero.EconomyHero;
import pl.psi.map.resources.Resources;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * Creature dwellings for the Bastion faction.
 *
 * Tier -> Building name          Base creature    Upgraded creature
 *   1  -> Centaur Stables        CENTAUR          BATTLE_CENTAUR
 *   2  -> Dwarf Cottage          DWARF            DWARF_WARRIOR
 *   3  -> Homestead              ELF              HIGH_ELF
 *   4  -> Pegasus Nest           PEGASUS          SILVER_PEGASUS
 *   5  -> Dendroid Arches        TREEMAN          ENT
 *   6  -> Unicorn Glade          UNICORN          BATTLE_UNICORN
 *   7  -> Dragon Cliffs          GREEN_DRAGON     GOLD_DRAGON
 */
public enum BastionUpgradeBuildings implements BuildingType {

    // Tier 1
    CENTAUR_STABLES(
            Set.of(BastionTownBuilding.FORT),
            new Resources(500, 5, 0, 0, 0, 0, 0),
            CreatureStatistic.CENTAUR, CreatureStatistic.BATTLE_CENTAUR),
    CENTAUR_STABLES_UPGRADED(
            Set.of(CENTAUR_STABLES),
            new Resources(1000, 5, 5, 0, 0, 0, 0),
            CreatureStatistic.CENTAUR, CreatureStatistic.BATTLE_CENTAUR),

    // Tier 2
    DWARF_COTTAGE(
            Set.of(CENTAUR_STABLES),
            new Resources(1000, 5, 5, 0, 0, 0, 0),
            CreatureStatistic.DWARF, CreatureStatistic.DWARF_WARRIOR),
    DWARF_COTTAGE_UPGRADED(
            Set.of(DWARF_COTTAGE),
            new Resources(1500, 5, 5, 0, 0, 0, 0),
            CreatureStatistic.DWARF, CreatureStatistic.DWARF_WARRIOR),

    // Tier 3
    HOMESTEAD(
            Set.of(DWARF_COTTAGE),
            new Resources(1500, 5, 5, 0, 0, 0, 0),
            CreatureStatistic.ELF, CreatureStatistic.HIGH_ELF),
    HOMESTEAD_UPGRADED(
            Set.of(HOMESTEAD),
            new Resources(1500, 0, 0, 4, 0, 0, 0),
            CreatureStatistic.ELF, CreatureStatistic.HIGH_ELF),

    // Tier 4
    PEGASUS_NEST(
            Set.of(DWARF_COTTAGE),
            new Resources(2000, 5, 5, 0, 0, 0, 0),
            CreatureStatistic.PEGASUS, CreatureStatistic.SILVER_PEGASUS),
    PEGASUS_NEST_UPGRADED(
            Set.of(PEGASUS_NEST, BastionTownBuilding.MYSTIC_POND),
            new Resources(2000, 5, 0, 4, 0, 0, 0),
            CreatureStatistic.PEGASUS, CreatureStatistic.SILVER_PEGASUS),

    // Tier 5
    DENDROID_ARCHES(
            Set.of(DWARF_COTTAGE),
            new Resources(2000, 0, 5, 4, 4, 0, 0),
            CreatureStatistic.TREEMAN, CreatureStatistic.ENT),
    DENDROID_ARCHES_UPGRADED(
            Set.of(DENDROID_ARCHES),
            new Resources(2000, 0, 5, 4, 4, 0, 0),
            CreatureStatistic.TREEMAN, CreatureStatistic.ENT),

    // Tier 6
    UNICORN_GLADE(
            Set.of(PEGASUS_NEST, DENDROID_ARCHES),
            new Resources(6000, 10, 10, 0, 0, 0, 0),
            CreatureStatistic.UNICORN, CreatureStatistic.BATTLE_UNICORN),
    UNICORN_GLADE_UPGRADED(
            Set.of(UNICORN_GLADE),
            new Resources(3000, 0, 5, 2, 2, 2, 2),
            CreatureStatistic.UNICORN, CreatureStatistic.BATTLE_UNICORN),

    // Tier 7
    DRAGON_CLIFFS(
            Set.of(UNICORN_GLADE),
            new Resources(10000, 5, 5, 5, 5, 5, 0),
            CreatureStatistic.GREEN_DRAGON, CreatureStatistic.GOLD_DRAGON),
    DRAGON_CLIFFS_UPGRADED(
            Set.of(DRAGON_CLIFFS),
            new Resources(15000, 5, 5, 20, 0, 0, 0),
            CreatureStatistic.GREEN_DRAGON, CreatureStatistic.GOLD_DRAGON);

    private final Set<BuildingType> prerequisites;
    private final Resources cost;
    private final CreatureStatistic baseCreature;
    private final CreatureStatistic upgradedCreature;

    BastionUpgradeBuildings(Set<BuildingType> prerequisites, Resources cost,
                            CreatureStatistic baseCreature, CreatureStatistic upgradedCreature) {
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.baseCreature = baseCreature;
        this.upgradedCreature = upgradedCreature;
    }

    @Override
    public Set<BuildingType> getPrerequisites() {
        return prerequisites;
    }

    @Override
    public void registerInTown(Town town) {
        town.addBastionUpgradeBuilding(this);
    }

    @Override
    public void applyEffect(Town town, EconomyHero hero) {
        // Creature dwellings produce units — handled by the recruitment shop
    }

    @Override
    public Resources getCost() {
        return cost;
    }

    @Override
    public boolean isBuiltIn(Town town) {
        return town.hasBuilt(this);
    }

    @Override
    public void buildIn(Town town, EconomyHero hero) {
        town.buildBuilding(this, hero);
    }

    public CreatureStatistic getBaseCreature() {
        return baseCreature;
    }

    public CreatureStatistic getUpgradedCreature() {
        return upgradedCreature;
    }

    public static Optional<BastionUpgradeBuildings> getBuildingForCreature(CreatureStatistic creature) {
        return Arrays.stream(values())
                .filter(b -> b.getBaseCreature() == creature || b.getUpgradedCreature() == creature)
                .filter(b -> {
                    // Base creature -> the dwelling whose prerequisites do NOT include
                    // its own upgraded counterpart (i.e. this is the tier's first building).
                    // Upgraded creature -> the dwelling that IS prerequisite-chained to
                    // the base dwelling of the SAME tier (same base/upgraded creature pair).
                    if (b.getBaseCreature() == creature) {
                        return b.getPrerequisites().stream()
                                .filter(p -> p instanceof BastionUpgradeBuildings)
                                .map(p -> (BastionUpgradeBuildings) p)
                                .noneMatch(other -> other.getBaseCreature() == b.getBaseCreature()
                                        && other.getUpgradedCreature() == b.getUpgradedCreature());
                    }
                    if (b.getUpgradedCreature() == creature) {
                        return b.getPrerequisites().stream()
                                .filter(p -> p instanceof BastionUpgradeBuildings)
                                .map(p -> (BastionUpgradeBuildings) p)
                                .anyMatch(other -> other.getBaseCreature() == b.getBaseCreature()
                                        && other.getUpgradedCreature() == b.getUpgradedCreature());
                    }
                    return false;
                })
                .findFirst();
    }
}