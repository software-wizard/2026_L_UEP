package pl.psi.economyAI;

import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.map.BoardEconomyEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Proste AI ekonomiczne szukające legalnych ruchów w swoim sąsiedztwie.
 */
public class EconomyAI {

    private final Random random = new Random();

    public EconomyAction decide(BoardEconomyEngine engine, EconomyHero aiHero, Point currentPos) {
        if (currentPos == null) {
            return new EconomyPassAction();
        }

        // Pobieramy sąsiadujące pola, na które możemy pójść
        List<Point> validMoves = new ArrayList<>();

        // Skanujemy najbliższe otoczenie (kratki 3x3)
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue;

                Point target = new Point(currentPos.getX() + x, currentPos.getY() + y);
                // Sprawdzamy używając Twojego nienaruszonego kodu!
                if (engine.canMove(target)) {
                    validMoves.add(target);
                }
            }
        }

        // Jeśli możemy gdzieś iść, wybieramy losowe legalne pole (lub pierwsze z brzegu)
        if (!validMoves.isEmpty()) {
            Point chosenTarget = validMoves.get(random.nextInt(validMoves.size()));
            return new EconomyMoveAction(chosenTarget);
        }

        // Jeśli nie mamy punktów ruchu albo jesteśmy zablokowani - pasujemy.
        return new EconomyPassAction();
    }
}

