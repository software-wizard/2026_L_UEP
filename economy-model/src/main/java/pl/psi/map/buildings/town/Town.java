package pl.psi.map.buildings.town;

import lombok.Getter;
import lombok.Setter;
import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.BuildingIf;
import pl.psi.map.buildings.enterAction.EnterAction;
import pl.psi.map.buildings.enterAction.EnterActionType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Town implements BuildingIf {

    @Getter
    @Setter
    EconomyHero owner;
    private final Set<TownBuilding> builtTownBuildings = new HashSet<>();
    private final Set<CreatureBuildings> builtCreatureBuildings = new HashSet<>();
    private final Set<TownCapability> activeCapabilities = new HashSet<>();
    private final Map<BuildingType, Integer> unitPool = new HashMap<>();

    public Town(EconomyHero owner) {
        this.builtTownBuildings.add(TownBuilding.VILLAGE_HALL);
        this.builtTownBuildings.add(TownBuilding.FORT);
        this.builtCreatureBuildings.add(CreatureBuildings.CURSED_TEMPLE);
        this.owner = owner;
    }

    //Budynki
    //Outside function,
    public void build(BuildingType building, EconomyHero hero) {
        buildBuilding(building, hero);
    }

    private void buildBuilding(BuildingType building, EconomyHero hero) {
        if (building.isBuiltIn(this)) {
            throw new IllegalStateException("Building already constructed.");
        }

        boolean allBuilt = building.getPrerequisites().stream()
                .allMatch(prereq -> prereq.isBuiltIn(this));

        if (!allBuilt) {
            throw new IllegalStateException("Prerequisites not met for " + building);
        }

        if (!hero.canAfford(building.getCost()))
        {
            throw new IllegalStateException("Can't afford " + building);
        }

        hero.pay(building.getCost());
        building.registerInTown(this);
        this.activeCapabilities.addAll(building.getProvidedCapabilities());
        if (!building.isUpgraded()) {
            unitPool.put(building, building.getGrowth());
        }
    }

    public boolean hasBuilt(TownBuilding building) {
        return builtTownBuildings.contains(building);
    }

    public boolean hasBuilt(CreatureBuildings building) {
        return builtCreatureBuildings.contains(building);
    }

    public boolean hasBuilt(BuildingType building) {
        return building.isBuiltIn(this);
    }

    public void addTownBuilding(TownBuilding building) {
        builtTownBuildings.add(building);
    }

    public void addCreatureBuilding(CreatureBuildings building) {
        builtCreatureBuildings.add(building);
    }

    //TESTING FUNCTION
    public void buildAllPrerequisites(BuildingType building) {
        for (BuildingType prereq : building.getPrerequisites()) {
            if (!this.hasBuilt(prereq)) {
                buildAllPrerequisites(prereq);
                this.build(prereq, owner);
            }
        }
    }

    //Jednostki
    public void generateUnits() {
        double modifier = getGrowthModifier(); // Bonusy z Fortu/Cytadeli/Zamku

        for (CreatureBuildings b : builtCreatureBuildings) {
            if (!b.isUpgraded()) {
                int growth = (int) (b.getGrowth() * modifier);
                int current = unitPool.getOrDefault(b, 0);
                addUnitToPool(b,growth);
            }
        }
    }

    private double getGrowthModifier() {
        if (activeCapabilities.contains(TownCapability.CASTLE_UPGRADE)) {
            return 2;
        } else if (activeCapabilities.contains(TownCapability.CITADEL_UPGRADE)) {
            return 1.5;
        }else{
            return 1;
        }
    }

    public int getAvailableUnits(CreatureBuildings building) {
        return unitPool.getOrDefault(building.getBaseBuilding(), 0);
    }

    public void buyUnits(CreatureBuildings building, int amount) {
        CreatureBuildings base = building.getBaseBuilding();
        int current = unitPool.getOrDefault(base, 0);
        if (current >= amount) {
            unitPool.put(base, current - amount);
        }
    }

    public void addUnitToPool(CreatureBuildings building, int amount) {
        int current = unitPool.getOrDefault(building, 0);
        unitPool.put(building, current + amount);
    }

    @Override
    public String getPath() {
        return "/objects/castle.png";
    }

    @Override
    public void endOfTurn() {
    }

    @Override
    public void generateResource() {
        for (TownBuilding building : builtTownBuildings) {
            building.generateResources(owner);
        }
    }

    public boolean hasCapability(TownCapability capability) {
        return activeCapabilities.contains(capability);
    }

    @Override
    public void interact(EconomyHero hero) {
    }

    @Override
    public typeOfObject getTypeOfObject() {
        return null;
    }

    @Override
    public EnterAction firstInteraction() {
        return new EnterAction(EnterActionType.OPEN_SHOP, this);
    }

    @Override
    public EnterAction secondInteraction() {
        return new EnterAction(EnterActionType.OPEN_UPGRADE, this);
    }
}
