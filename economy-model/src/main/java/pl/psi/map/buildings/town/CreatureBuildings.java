package pl.psi.map.buildings.town;

import lombok.Getter;
import pl.psi.creatures.CreatureStatistic;
import pl.psi.hero.EconomyHero;
import pl.psi.map.resources.Resources;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public enum CreatureBuildings implements BuildingType {
    // POZIOM 1
    CURSED_TEMPLE(Set.of(TownBuilding.FORT), new Resources(400, 5, 5, 0, 0, 0, 0), CreatureStatistic.SKELETON, 12, null),
    CURSED_TEMPLE_UPGRADED(Set.of(CURSED_TEMPLE), new Resources(1000, 5, 5, 0, 0, 0, 0), CreatureStatistic.SKELETON_WARRIOR, null, CURSED_TEMPLE),

    // POZIOM 2
    GRAVEYARD(Set.of(CURSED_TEMPLE), new Resources(1000, 0, 5, 0, 0, 0, 0), CreatureStatistic.WALKING_DEAD, 8, null),
    GRAVEYARD_UPGRADED(Set.of(GRAVEYARD), new Resources(1000, 5, 5, 0, 0, 0, 0), CreatureStatistic.ZOMBIE, null, GRAVEYARD),

    // POZIOM 3
    TOMB_OF_SOULS(Set.of(GRAVEYARD), new Resources(1500, 5, 5, 0, 0, 0, 0), CreatureStatistic.WIGHT, 7, null),
    TOMB_OF_SOULS_UPGRADED(Set.of(TOMB_OF_SOULS), new Resources(1500, 0, 0, 4, 0, 0, 0), CreatureStatistic.WRAITH, null, TOMB_OF_SOULS),

    // POZIOM 4
    ESTATE(Set.of(GRAVEYARD), new Resources(2000, 5, 5, 0, 0, 0, 0), CreatureStatistic.VAMPIRE, 4, null),
    ESTATE_UPGRADED(Set.of(ESTATE, TownBuilding.NECROMANCY_AMPLIFIER), new Resources(2000, 10, 0, 4, 0, 10, 10), CreatureStatistic.VAMPIRE_LORD, null, ESTATE),

    // POZIOM 5
    MAUSOLEUM(Set.of(GRAVEYARD), new Resources(2000, 0, 4, 4, 4, 0, 0), CreatureStatistic.LICH, 3, null),
    MAUSOLEUM_UPGRADED(Set.of(MAUSOLEUM), new Resources(2000, 0, 4, 4, 4, 0, 0), CreatureStatistic.POWER_LICH, null, MAUSOLEUM),

    // POZIOM 6
    HALL_OF_DARKNESS(Set.of(ESTATE, MAUSOLEUM), new Resources(6000, 10, 10, 4, 0, 0, 0), CreatureStatistic.BLACK_KNIGHT, 2, null),
    HALL_OF_DARKNESS_UPGRADED(Set.of(HALL_OF_DARKNESS), new Resources(3000, 0, 5, 2, 2, 2, 2), CreatureStatistic.DREAD_KNIGHT, null, HALL_OF_DARKNESS),

    // POZIOM 7
    DRAGON_VAULT(Set.of(HALL_OF_DARKNESS), new Resources(10000, 5, 5, 5, 5, 5, 0), CreatureStatistic.BONE_DRAGON, 1, null),
    DRAGON_VAULT_UPGRADED(Set.of(DRAGON_VAULT), new Resources(15000, 5, 5, 20, 0, 0, 0), CreatureStatistic.GHOST_DRAGON, null, DRAGON_VAULT);

    @Getter
    private final Set<BuildingType> prerequisites;
    @Getter
    private final Resources cost;
    @Getter
    private final CreatureStatistic creature;
    private final Integer growth;
    private final CreatureBuildings baseBuilding;

    CreatureBuildings(Set<BuildingType> prerequisites, Resources cost,
                      CreatureStatistic creature, Integer growth, CreatureBuildings baseBuilding) {
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.creature = creature;
        this.growth = growth;
        this.baseBuilding = baseBuilding;
    }

    public int getGrowth() {
        // Jeśli to ulepszenie, pobierz wzrost z bazy
        return baseBuilding == null ? growth : baseBuilding.growth;
    }

    public CreatureBuildings getBaseBuilding() {
        // Zwraca siebie jeśli jest bazą, lub referencję do bazy
        return baseBuilding == null ? this : baseBuilding;
    }

    public boolean isUpgraded() {
        return baseBuilding != null;
    }

    @Override
    public Set<TownCapability> getProvidedCapabilities() {
        return Set.of();
    }

    @Override
    public void generateResources(EconomyHero hero) {
    }

    public static Optional<CreatureBuildings> getBuildingForCreature(CreatureStatistic creature) {
        return Arrays.stream(values())
                .filter(b -> b.getCreature() == creature)
                .findFirst();
    }
}