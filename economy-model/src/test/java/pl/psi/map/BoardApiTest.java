package pl.psi.map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.map.buildings.town.Town;
import pl.psi.map.resources.Resources;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BoardApiTest {

    private EconomyHero hero1;
    private EconomyHero hero2;
    private Town town;
    private Resources startingResources;

    @BeforeEach
    void setUp() {
        town = new Town(hero1);
        startingResources = new Resources(100000, 100, 100, 100, 100, 100, 100);
        Statistics aStats = new Statistics(10, 10, 10, 10);
        hero1 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, startingResources, aStats);
    }

    @Test
    void shouldCallInteractOnMapObjectWhenInteracted() {
        // given
        Point interactionPoint = new Point(1, 1);
        MapObjectIf mapObject = spy(new testMapObject());

        Map<Point, MapObjectIf> map = new HashMap<>();
        map.put(interactionPoint, mapObject);

        BoardEconomyEngine engine = new BoardEconomyEngine(hero1, hero2, map);

        // when
        engine.interact(interactionPoint);

        // then
        verify(mapObject, times(1)).interact(eq(hero1));
    }

    @Test
    void shouldCallInteractOnMapObjectWhenWalkedOver() {
        // given
        Point interactionPoint = new Point(1, 1);
        MapObjectIf mapObject = spy(new testMapObject());

        Map<Point, MapObjectIf> map = new HashMap<>();
        map.put(interactionPoint, mapObject);

        BoardEconomy board = BoardEconomy.builder()
                .addHero(hero1, new Point(0,0))
                .addHero(hero2,new Point(14,14))
                .addInteractables(map)
                .build();


        // when
        board.move(hero1, interactionPoint);
        board.interact(hero1, interactionPoint);

        // then
        verify(mapObject, times(1)).interact(eq(hero1));
    }

    @Test
    void shouldDissapearMapObjectWhenInteractedIfHasPickupable() {
        // given
        Point interactionPoint = new Point(1, 1);
        MapObjectIf mapObject = spy(new testMapObjectPickupable());

        Map<Point, MapObjectIf> map = new HashMap<>();
        map.put(interactionPoint, mapObject);

        BoardEconomyEngine engine = new BoardEconomyEngine(hero1, hero2, map);

        // when
        engine.interact(interactionPoint);

        // then
        verify(mapObject, times(1)).interact(eq(hero1));
        assertThat(engine.getMapObject(interactionPoint).isEmpty()).isTrue();
    }

    @Test
    void shouldStayMapObjectWhenInteractedIfHasPickupable() {
        // given
        Point interactionPoint = new Point(1, 1);
        MapObjectIf mapObject = spy(new testMapObject());

        Map<Point, MapObjectIf> map = new HashMap<>();
        map.put(interactionPoint, mapObject);

        BoardEconomyEngine engine = new BoardEconomyEngine(hero1, hero2, map);

        // when
        engine.interact(interactionPoint);

        // then
        verify(mapObject, times(1)).interact(eq(hero1));
        assertThat(engine.getMapObject(interactionPoint).isEmpty()).isFalse();
    }

    @Test
    void testTownInteractions() {
        Resources res = new Resources(1000, 10, 10, 10, 10, 10, 10);
        Statistics stats = new Statistics(10, 10, 10, 10);
        EconomyHero h1 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, res, stats);
        EconomyHero h2 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, res, stats);

        Map<Point, MapObjectIf> map = new HashMap<>();
        Town town1 = new Town(h1);
        Point town1Pos = new Point(17, 1);
        map.put(town1Pos, town1);

        BoardEconomyEngine engine = new BoardEconomyEngine(h1, h2, map);

        System.out.println("DEBUG INITIAL STATE:");
        System.out.println("canMove(17,1): " + engine.canMove(town1Pos));
        System.out.println("canAttack(17,1): " + engine.canAttack(town1Pos));
        System.out.println("canEnter(17,1): " + engine.canEnter(town1Pos));
        System.out.println("canInteract(17,1): " + engine.canInteract(town1Pos));

        h1.setRemainingMoves(100);
        engine.move(town1Pos);

        System.out.println("DEBUG AFTER MOVING TO TOWN:");
        System.out.println("Hero at Town is present: " + engine.getHero(town1Pos).isPresent());
        System.out.println("canMove(17,1): " + engine.canMove(town1Pos));
        System.out.println("canAttack(17,1): " + engine.canAttack(town1Pos));
        System.out.println("canEnter(17,1): " + engine.canEnter(town1Pos));
        System.out.println("canInteract(17,1): " + engine.canInteract(town1Pos));
    }

    @Test
    void testGrailDigging() {
        Resources res = new Resources(1000, 10, 10, 10, 10, 10, 10);
        Statistics stats = new Statistics(10, 10, 10, 10);
        EconomyHero h1 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, res, stats);
        EconomyHero h2 = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, res, stats);

        Map<Point, MapObjectIf> map = new HashMap<>();
        pl.psi.map.Grail grail = new pl.psi.map.Grail();
        Point grailPos = new Point(2, 3);
        map.put(grailPos, grail);

        BoardEconomyEngine engine = new BoardEconomyEngine(h1, h2, map);

        // Initially h1 hasn't dug and doesn't have Grail
        assertThat(h1.isHasGrail()).isFalse();
        assertThat(h1.isHasDugThisTurn()).isFalse();

        // 1. Digging on empty tile (0, 0) should throw exception
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> engine.dig());
        // Even though it failed, hasDugThisTurn is set to true!
        assertThat(h1.isHasDugThisTurn()).isTrue();

        // 2. Trying to dig again throws exception because we dug already this turn
        org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> engine.dig());

        // 3. Reset turn (pass)
        engine.pass(); // switches to h2
        engine.pass(); // switches back to h1
        assertThat(h1.isHasDugThisTurn()).isFalse();

        // 4. Move to Grail position (2, 3) and dig
        h1.setRemainingMoves(100);
        engine.move(grailPos);
        engine.dig();

        // Hero now has the Grail and hasDugThisTurn is true
        assertThat(h1.isHasGrail()).isTrue();
        assertThat(h1.isHasDugThisTurn()).isTrue();
        // Grail should be removed from board
        assertThat(engine.getMapObject(grailPos).isEmpty()).isTrue();
    }
}

