package pl.psi.map.buildings.town;

import pl.psi.hero.EconomyHero;
import pl.psi.map.resources.Resources;

import java.util.Set;

/**
 * Town buildings specific to the Bastion faction.
 * Common buildings (Fort, Mage Guild, etc.) are shared via TownBuilding.
 * This enum covers the Bastion-unique building: the Dendroid Saplings.
 */
public enum BastionTownBuilding implements BuildingType {

    // FORTIFICATIONS - shared concept, Bastion flavour names
    FORT(Set.of(), new Resources(5000, 20, 20, 0, 0, 0, 0), Category.COMMON),
    CITADEL(Set.of(FORT), new Resources(2500, 0, 5, 0, 0, 0, 0), Category.COMMON),
    CASTLE(Set.of(CITADEL), new Resources(5000, 10, 10, 0, 0, 0, 0), Category.COMMON),

    // RESOURCES
    MARKETPLACE(Set.of(), new Resources(500, 5, 0, 0, 0, 0, 0), Category.RESOURCE),
    RESOURCE_SILO(Set.of(MARKETPLACE), new Resources(5000, 0, 5, 0, 0, 0, 0), Category.RESOURCE),

    // MAGE GUILD
    MAGE_GUILD_LVL_1(Set.of(), new Resources(2000, 5, 5, 0, 0, 0, 0), Category.MAGE_GUILD),
    MAGE_GUILD_LVL_2(Set.of(MAGE_GUILD_LVL_1), new Resources(1000, 5, 5, 4, 4, 4, 4), Category.MAGE_GUILD),
    MAGE_GUILD_LVL_3(Set.of(MAGE_GUILD_LVL_2), new Resources(1000, 5, 5, 6, 6, 6, 6), Category.MAGE_GUILD),
    MAGE_GUILD_LVL_4(Set.of(MAGE_GUILD_LVL_3), new Resources(1000, 5, 5, 8, 8, 8, 8), Category.MAGE_GUILD),
    MAGE_GUILD_LVL_5(Set.of(MAGE_GUILD_LVL_4), new Resources(1000, 5, 5, 10, 10, 10, 10), Category.MAGE_GUILD),

    // COMMON BUILDINGS
    TAVERN(Set.of(), new Resources(500, 5, 0, 0, 0, 0, 0), Category.COMMON),
    TOWN_HALL(Set.of(TAVERN), new Resources(2500, 0, 0, 0, 0, 0, 0), Category.COMMON),
    BLACKSMITH(Set.of(), new Resources(1000, 5, 0, 0, 0, 0, 0), Category.COMMON),
    CITY_HALL(Set.of(TOWN_HALL, MAGE_GUILD_LVL_1, MARKETPLACE, BLACKSMITH), new Resources(5000, 0, 0, 0, 0, 0, 0), Category.COMMON),
    CAPITOL(Set.of(CITY_HALL, CASTLE), new Resources(10000, 0, 0, 0, 0, 0, 0), Category.COMMON),

    // BASTION-SPECIFIC
    // Raises the luck of all creatures in a Bastion hero's army by +1
    MYSTIC_POND(Set.of(MARKETPLACE), new Resources(1000, 0, 0, 0, 0, 0, 0), Category.TOWN_SPECIFIC),
    // Raises morale of all creatures in a Bastion hero's army by +1
    RALLY_FLAG(Set.of(TAVERN), new Resources(1000, 5, 0, 0, 0, 0, 0), Category.TOWN_SPECIFIC);

    public enum Category {
        GRAIL,
        CREATURE_GENERATOR,
        COMMON,
        TOWN_SPECIFIC,
        MAGE_GUILD,
        RESOURCE
    }

    private final Set<BuildingType> prerequisites;
    private final Resources cost;
    private final Category category;

    BastionTownBuilding(Set<BuildingType> prerequisites, Resources cost, Category category) {
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.category = category;
    }

    @Override
    public Set<BuildingType> getPrerequisites() {
        return prerequisites;
    }

    @Override
    public void registerInTown(Town town) {
        town.addBastionTownBuilding(this);
    }

    @Override
    public void applyEffect(Town town, EconomyHero hero) {
        switch (this) {
            case FORT:
            case CITADEL:
            case CASTLE:
            case MARKETPLACE:
            case BLACKSMITH:
                break;
            case RESOURCE_SILO:
                hero.addResource(new Resources(0, 1, 1, 0, 0, 0, 0));
                break;
            case MAGE_GUILD_LVL_1:
            case MAGE_GUILD_LVL_2:
            case MAGE_GUILD_LVL_3:
            case MAGE_GUILD_LVL_4:
            case MAGE_GUILD_LVL_5:
                break;
            case TAVERN:
                break;
            case TOWN_HALL:
                hero.addResource(new Resources(1000, 0, 0, 0, 0, 0, 0));
                break;
            case CITY_HALL:
                hero.addResource(new Resources(2000, 0, 0, 0, 0, 0, 0));
                break;
            case CAPITOL:
                hero.addResource(new Resources(4000, 0, 0, 0, 0, 0, 0));
                break;
            case MYSTIC_POND:
                // TODO: apply +1 luck to hero's army
                break;
            case RALLY_FLAG:
                // TODO: apply +1 morale to hero's army
                break;
        }
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

    public Category getCategory() {
        return category;
    }
}


