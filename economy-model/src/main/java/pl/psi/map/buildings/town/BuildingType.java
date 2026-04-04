package pl.psi.map.buildings.town;

import pl.psi.hero.EconomyHero;
import pl.psi.map.resources.Resources;

import java.util.Set;

public interface BuildingType {
    void generateResources(EconomyHero hero);
    Resources getCost();
    boolean isBuiltIn(Town town);
    void registerInTown(Town town);
    Set<BuildingType> getPrerequisites();
    Set<TownCapability> getProvidedCapabilities();
    boolean isUpgraded();
    int getGrowth();
}
