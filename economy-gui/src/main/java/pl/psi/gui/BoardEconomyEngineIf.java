package pl.psi.gui;

import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.map.MapObjectIf;
import pl.psi.map.buildings.BuildingIf;
import pl.psi.map.buildings.town.Town;

import java.beans.PropertyChangeListener;
import java.util.Optional;

public interface BoardEconomyEngineIf {
    boolean canMove(Point point);
    void move(Point point);
    boolean canInteract(Point point);
    void interact(Point point);
    boolean canEnter(Point point);
    void enter(Point point);
    void pass();
    void secondInteraction(Point point);
    boolean isCurrentHero(Point point);
    boolean isHero(Point point);
    boolean isEnterable(Point point);
    boolean isHeroAdjacent(Point target);
    EconomyHero getCurrentHero();
    Optional<MapObjectIf> getMapObject(Point point);
    void addObserver(PropertyChangeListener aObserver);
    Optional<Town> getTownUnderHero(EconomyHero aCurrentHero);
    void openShop(BuildingIf buildingOpt);
    void openUpgrades(BuildingIf buildingOpt);
    void enterBank(BuildingIf building);
    boolean canAttack(Point point);
    String getMapObjectPath(Point point);
}