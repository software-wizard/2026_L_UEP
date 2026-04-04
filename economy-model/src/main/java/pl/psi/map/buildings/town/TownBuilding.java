package pl.psi.map.buildings.town;

import pl.psi.hero.EconomyHero;
import pl.psi.map.resources.Resources;

import java.util.Set;

public enum TownBuilding implements BuildingType {

    //TODO ADD WHAT EACH TOWN UPGRADE DOES

    //MISCELLANEOUS BUILDINGS
    TAVERN(Set.of(), new Resources(500, 5,0,0,0,0,0), Category.MISCELLANEOUS),
    MARKETPLACE(Set.of(), new Resources(500,5,0,0,0,0,0), Category.MISCELLANEOUS),
    RESOURCE_SILO(Set.of(MARKETPLACE), new Resources(5000,0,5,0,0,0,0), Category.MISCELLANEOUS),
    BLACKSMITH(Set.of(),new Resources(1000,5,0,0,0,0,0),Category.MISCELLANEOUS),

    //FORTIFICATIONS
    FORT(Set.of(),new Resources(5000,20,20,0,0,0,0),Category.FORTIFICATIONS),
    CITADEL(Set.of(FORT), new Resources(2500,0,5,0,0,0,0), Category.FORTIFICATIONS),
    CASTLE(Set.of(CITADEL), new Resources(5000,10,10,0,0,0,0), Category.FORTIFICATIONS),

    //MAGE GUILD
    MAGE_GUILD_LVL_1(Set.of(), new Resources(2000, 5,5,0,0,0,0), Category.MAGE_GUILD),
    MAGE_GUILD_LVL_2(Set.of(MAGE_GUILD_LVL_1), new Resources(1000,5,5,4,4,4,4), Category.MAGE_GUILD),
    MAGE_GUILD_LVL_3(Set.of(MAGE_GUILD_LVL_2), new Resources(1000,5,5,6,6,6,6), Category.MAGE_GUILD),
    MAGE_GUILD_LVL_4(Set.of(MAGE_GUILD_LVL_3), new Resources(1000,5,5,8,8,8,8), Category.MAGE_GUILD),
    MAGE_GUILD_LVL_5(Set.of(MAGE_GUILD_LVL_4), new Resources(1000,5,5,10,10,10,10), Category.MAGE_GUILD),

    //HALLS
    VILLAGE_HALL(Set.of(), new Resources(0,0,0,0,0,0,0),Category.HALLS),
    TOWN_HALL(Set.of(TAVERN), new Resources(2500,0,0,0,0,0,0), Category.HALLS),
    CITY_HALL(Set.of(TOWN_HALL, MAGE_GUILD_LVL_1,MARKETPLACE,BLACKSMITH), new Resources(5000,0,0,0,0,0,0), Category.HALLS),
    CAPITOL(Set.of(CITY_HALL,CASTLE), new Resources(10000,0,0,0,0,0,0), Category.HALLS),

    NECROMANCY_AMPLIFIER(Set.of(MAGE_GUILD_LVL_1),new Resources(1000,0,0,0,0,0,0) ,Category.UNIQUE);

    public enum Category {
        GRAIL,
        HALLS,
        FORTIFICATIONS,
        MISCELLANEOUS,
        MAGE_GUILD,
        UNIQUE
    }

    private final Set<BuildingType> prerequisites;
    private final Resources cost;
    private final Category category;

    TownBuilding(Set<BuildingType> prerequisites, Resources cost, Category category) {
        this.prerequisites = prerequisites;
        this.cost = cost;
        this.category = category;
    }

    public Set<BuildingType> getPrerequisites() {
        return prerequisites;
    }

    @Override
    public void registerInTown(Town town) {
        town.addTownBuilding(this);
    }

    @Override
    public Set<TownCapability> getProvidedCapabilities() {
        switch (this) {
            case MAGE_GUILD_LVL_1:
                return Set.of(TownCapability.SPELL_PURCHASE);
            case MARKETPLACE:
                return Set.of(TownCapability.RESOURCE_TRADE);
            case BLACKSMITH:
                return Set.of(TownCapability.WARMACHINE_PURCHASE);
            case FORT:
                return Set.of(TownCapability.FORT_UPGRADE);
            case CITADEL:
                return Set.of(TownCapability.CITADEL_UPGRADE);
            case CASTLE:
                return Set.of(TownCapability.CASTLE_UPGRADE);
            default:
                return Set.of();
        }
    }

    @Override
    public boolean isUpgraded() {
        return false;
    }

    @Override
    public int getGrowth() {
        return 0;
    }

    @Override
    public void generateResources(EconomyHero hero) {
        if (hero == null) {
            throw new NullPointerException("EconomyHero argument is null");
        }
        switch (this){
            case VILLAGE_HALL:
                hero.addResource(new Resources(500,0,0,0,0,0,0));
                break;
            case TOWN_HALL:
                hero.addResource(new Resources(1000,0,0,0,0,0,0));
                break;
            case CITY_HALL:
                hero.addResource(new Resources(2000,0,0,0,0,0,0));
                break;
            case CAPITOL:
                hero.addResource(new Resources(4000,0,0,0,0,0,0));
                break;
            case RESOURCE_SILO:
                hero.addResource(new Resources(0,1,1,0,0,0,0));
                break;
        }
    }


    public Resources getCost() {
        return cost;
    }

    @Override
    public boolean isBuiltIn(Town town) {
        return town.hasBuilt(this);
    }

    public void buildIn(Town town, EconomyHero hero) {
        town.build(this, hero);
    }

    public Category getCategory() {
        return category;
    }
}


