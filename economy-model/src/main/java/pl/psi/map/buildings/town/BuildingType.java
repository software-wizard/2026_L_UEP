package pl.psi.map.buildings.town;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.psi.hero.EconomyHero;
import pl.psi.map.resources.Resources;

import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TownBuilding.class, name = "TownBuilding"),
        @JsonSubTypes.Type(value = CreatureBuildings.class, name = "CreatureBuildings")
})
public interface BuildingType {
    void generateResources(EconomyHero hero);
    Resources getCost();
    Set<BuildingType> getPrerequisites();
    Set<TownCapability> getProvidedCapabilities();
    boolean isUpgraded();
    int getGrowth();
}
