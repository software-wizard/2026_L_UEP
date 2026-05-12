package pl.psi.map;

import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.enterAction.EnterAction;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = pl.psi.map.buildings.town.Town.class, name = "Town"),
        @JsonSubTypes.Type(value = pl.psi.map.buildings.bank.Bank.class, name = "Bank"),
        @JsonSubTypes.Type(value = pl.psi.map.resources.Gold.class, name = "Gold"),
        @JsonSubTypes.Type(value = pl.psi.map.resources.generators.ResourceGenerator.class, name = "ResourceGenerator"),
        @JsonSubTypes.Type(value = pl.psi.hero.artifacts.Artifact.class, name = "Artifact"),
        @JsonSubTypes.Type(value = pl.psi.hero.artifacts.EconomySpell.class, name = "EconomySpell")
})
public interface MapObjectIf {
    String getPath();
    void endOfTurn(); // w tych bez wydarzeń na koniec zostaje pusty
    void generateResource();
    void generateUnits();
    void interact(EconomyHero hero);
    typeOfObject getTypeOfObject();
    EconomyHero getOwner();
    EnterAction firstInteraction();
    EnterAction secondInteraction();
    void resetBuildingOption();
    //TODO dopisać te metody, na koniec tury musi być wywoływane
    enum typeOfObject{
        GENERATOR,
        BUILDING,
        PICKUPABLE,
        UNKNOWN
    }
}
