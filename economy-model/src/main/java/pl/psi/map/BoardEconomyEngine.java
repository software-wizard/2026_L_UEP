package pl.psi.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.enterAction.EnterAction;
import pl.psi.map.buildings.BuildingIf;
import pl.psi.economy.Point;
import pl.psi.map.buildings.town.Town;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

public class BoardEconomyEngine {

    public static final String HERO_MOVED = "HERO_MOVED";
    private final TurnQueueEconomy turnQueue;
    private final BoardEconomy board;
    @JsonIgnore
    private final PropertyChangeSupport observerSupport = new PropertyChangeSupport(this);
    Map<Point, MapObjectIf> interactables;
    private int turnCounter;
    private int dayCounter;
    @JsonIgnore
    private final Map<EconomyHero, Set<Point>> revealedPoints = new HashMap<>();


    public BoardEconomyEngine(final EconomyHero hero1, final EconomyHero hero2, Map<Point, MapObjectIf> map) {
        this.interactables = map;
        turnQueue = new TurnQueueEconomy(hero1, hero2);
        board = BoardEconomy.builder()
                .addHero(hero1, new Point(0,0))
                .addHero(hero2,new Point(17,8))
                .addInteractables(map)
                .build();
        updateVisibility(hero1);
        updateVisibility(hero2);
    }

    public boolean canMove(final Point point) {
        if (!isHero(point)) {
            return board.canMove(turnQueue.getCurrentHero(), point);
        }
        return false;
    }

    public boolean canAttack(final Point point) {
        return isHeroAdjacent(point) && getHero(point).isPresent();
    }

    public boolean canEnter(final Point point) {
        if (isEnterable(point)) {
            return isCurrentHero(point) || (!isHero(point) && canMove(point));
        }
        return false;
    }

    public boolean canInteract(Point point) {
        if (getInteractable(point).isPresent()) {
            return isCurrentHero(point) || (!isHero(point) && canMove(point));
        }
        return false;
    }

    public void move(final Point point) {
        board.move(turnQueue.getCurrentHero(), point);
        updateVisibility(turnQueue.getCurrentHero());
        observerSupport.firePropertyChange(HERO_MOVED, null, point);
    }

    public void interact(final Point point) {
        if (isCurrentHero(point)) {
            board.interact(turnQueue.getCurrentHero(), point);
        } else if (canMove(point)) {
            move(point);
            if (isCurrentHero(point)) {
                board.interact(turnQueue.getCurrentHero(), point);
            }
        }
    }

    public void enter(final Point point) {
        if (isCurrentHero(point)) {
            executeEnter(point);
        } else if (canMove(point)) {
            move(point);
            if (isCurrentHero(point)) {
                executeEnter(point);
            }
        }
    }

    private void executeEnter(final Point point) {
        EnterAction action = board.enter(point);
        switch (action.getType()) {
            case OPEN_SHOP: {
                openShop(action.getBuilding());
                break;
            }
            case ENTER_BANK: {
                enterBank(action.getBuilding());
                break;
            }
        }
    }

    public void secondInteraction(final Point point) {
        if (isCurrentHero(point)) {
            executeSecondInteraction(point);
        } else if (canMove(point)) {
            move(point);
            if (isCurrentHero(point)) {
                executeSecondInteraction(point);
            }
        }
    }

    private void executeSecondInteraction(final Point point) {
        EnterAction action = board.secondInteraction(point);
        switch (action.getType()) {
            case OPEN_UPGRADE:{
                openUpgrades(action.getBuilding());
                break;
            }
        }
    }


    public Optional<EconomyHero> getHero(final Point point) {
        return board.getHero(point);
    }

    public EconomyHero getCurrentHero() {
        return turnQueue.getCurrentHero();
    }

    public Optional<MapObjectIf> getMapObject(final Point point) {
        return board.getObjectAt(point);
    }

    public Optional<InteractableIf> getInteractable(final Point point) {
        return board.getInteractableAt(point);
    }

    public void pass() {
        getCurrentHero().resetMoveRange();
        getCurrentHero().setHasDugThisTurn(false);
        endOfTurn();
        turnQueue.next();
        updateVisibility(getCurrentHero());
    }

    public void dig() {
        EconomyHero hero = getCurrentHero();
        if (hero.isHasDugThisTurn()) {
            throw new IllegalStateException("You can only dig once per turn!");
        }
        hero.setHasDugThisTurn(true);
        Point heroPos = board.getPosition(hero);
        if (heroPos != null) {
            Optional<MapObjectIf> objOpt = board.getObjectAt(heroPos);
            if (objOpt.isPresent() && objOpt.get() instanceof pl.psi.map.Grail) {
                board.interact(hero, heroPos);
            } else {
                throw new IllegalStateException("There is nothing buried here!");
            }
        } else {
            throw new IllegalStateException("Hero position is not set!");
        }
    }

    private void endOfTurn() { // called after each click of the pass button
        turnCounter++;
        System.out.println("End of turn");
        if (turnCounter >= 2){
            turnCounter = 0;
            endOfDay();
        }
    }

    public void endOfDay(){ // called after both players pass
        dayCounter++;
        if (dayCounter >= 7){
            dayCounter = 0;
            endOfWeek();
        }
        System.out.println("End of day");
        generateResourcesEndDay();
        resetBuldingOptionAtTowns();
    }

    private void resetBuldingOptionAtTowns() {
        for (MapObjectIf mapObject : interactables.values()) {
            mapObject.resetBuildingOption();
        }
    }

    private void endOfWeek(){
        System.out.println("End of week");
        createUnitsAtTowns();
    }

    private void createUnitsAtTowns() {
        for (MapObjectIf mapObject : interactables.values()) {
            mapObject.generateUnits();
        }
    }

    private void generateResourcesEndDay(){
        for (MapObjectIf interactable : interactables.values()) {
            interactable.generateResource();
        }
        for (EconomyHero hero : board.getHeroes()) {
            hero.generateResourcesFromArtifacts();
        }
    }

    public void addObserver(final PropertyChangeListener aObserver) {
        observerSupport.addPropertyChangeListener(aObserver);
        turnQueue.addObserver(aObserver);
    }

    public boolean isCurrentHero(Point point) {
        return Optional.of(turnQueue.getCurrentHero()).equals(board.getHero(point));
    }

    public boolean isHero(Point point) {
        return board.getHero(point).isPresent();
    }

    public boolean isEnterable(final Point point) {
        return board.getBuildingAt(point).isPresent();
    }

    public boolean isHeroAdjacent(Point target) {
        Point heroPos = board.getPosition(getCurrentHero());
        return heroPos.distance(target) == 1; // Manhattan distance
    }


    public void openShop(BuildingIf buildingOpt) {
        observerSupport.firePropertyChange("OPEN_SHOP", null, new Object[]{getCurrentHero(), buildingOpt});
    }

    public void openUpgrades(BuildingIf buildingOpt) {
        observerSupport.firePropertyChange("OPEN_UPGRADES", null, new Object[]{getCurrentHero(),buildingOpt});
    }

    public void enterBank(BuildingIf building){
        observerSupport.firePropertyChange("ENTER_BANK", null, new Object[]{getCurrentHero(), building});
    }

    public Optional<Town> getTownUnderHero(EconomyHero aCurrentHero) {
        Point pos = board.getPosition(aCurrentHero);
        return board.getBuildingAt(pos)
                .filter(Town.class::isInstance)
                .map(Town.class::cast);
    }

    public boolean isTileVisible(Point point) {
        EconomyHero currentHero = getCurrentHero();
        if (currentHero == null) {
            return true;
        }
        Set<Point> revealed = revealedPoints.get(currentHero);
        return revealed != null && revealed.contains(point);
    }

    public void updateVisibility(EconomyHero hero) {
        if (hero == null) return;
        Set<Point> revealed = revealedPoints.computeIfAbsent(hero, h -> new HashSet<>());
        Point heroPos = board.getPosition(hero);
        if (heroPos != null) {
            int radius = hero.getVisibilityRadius();
            revealArea(heroPos, radius, revealed);
        }
        for (Map.Entry<Point, MapObjectIf> entry : interactables.entrySet()) {
            MapObjectIf obj = entry.getValue();
            if (obj instanceof Town && ((Town) obj).getOwner() == hero) {
                Point townPos = entry.getKey();
                revealArea(townPos, 3, revealed);
            }
        }
    }

    private void revealArea(Point center, int radius, Set<Point> revealed) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (Math.max(Math.abs(dx), Math.abs(dy)) <= radius) {
                    revealed.add(new Point(center.getX() + dx, center.getY() + dy));
                }
            }
        }
    }
}

