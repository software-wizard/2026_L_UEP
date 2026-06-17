package pl.psi.economyAI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.map.BoardEconomyEngine;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EconomyAITest {

    private EconomyAI economyAI;
    private BoardEconomyEngine engineMock;
    private EconomyHero heroMock;

    @BeforeEach
    void setUp() {
        economyAI = new EconomyAI();
        // Używamy Mockito, żeby sztucznie zasymulować środowisko (BoardEconomyEngine), bez ładowania całego okna gry
        engineMock = mock(BoardEconomyEngine.class);
        heroMock = mock(EconomyHero.class);
    }

    @Test
    void shouldPassWhenCurrentPosIsNull() {
        EconomyAction action = economyAI.decide(engineMock, heroMock, null);
        assertTrue(action instanceof EconomyPassAction, "Jeśli brak punktu startowego, AI ma pasować.");
    }

    @Test
    void shouldPassWhenNoMovesAreValid() {
        Point start = new Point(5, 5);
        // Symulacja: żadne sąsiadujące pole nie jest dostępne (np. wyczerpane punkty ruchu lub otoczony blokami)
        when(engineMock.canMove(any(Point.class))).thenReturn(false);

        EconomyAction action = economyAI.decide(engineMock, heroMock, start);
        assertTrue(action instanceof EconomyPassAction, "Jeśli bohater nie ma legalnych ruchów, AI musi spasować.");
    }

    @Test
    void shouldMoveWhenMovesAreValid() {
        Point start = new Point(5, 5);
        // Symulacja: AI ma miejsce na ruch dookoła
        when(engineMock.canMove(any(Point.class))).thenReturn(true);

        EconomyAction action = economyAI.decide(engineMock, heroMock, start);
        assertTrue(action instanceof EconomyMoveAction, "Jeśli są możliwe legalne ruchy, AI decyduje się na poruszenie.");
    }
}
