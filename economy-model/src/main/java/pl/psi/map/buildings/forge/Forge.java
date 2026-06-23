package pl.psi.map.buildings.forge;

import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.BuildingIf;
import pl.psi.map.buildings.enterAction.EnterAction;
import pl.psi.map.buildings.enterAction.EnterActionType;

public class Forge implements BuildingIf {

    @Override
    public String getPath() {
        return "/objects/forge.png";
    }

    @Override
    public EnterAction firstInteraction() {
        return new EnterAction(EnterActionType.OPEN_FORGE, this);
    }

    @Override
    public void endOfTurn() {}

    @Override
    public void generateResource() {}

    @Override
    public void generateUnits() {}

    @Override
    public void interact(EconomyHero hero) {}

    @Override
    public typeOfObject getTypeOfObject() {
        return null;
    }

    @Override
    public EconomyHero getOwner() {
        return null;
    }

    @Override
    public EnterAction secondInteraction() {
        return null;
    }

    @Override
    public void resetBuildingOption() {}
}