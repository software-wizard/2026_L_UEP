package pl.psi.economyAI;

import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.map.BoardEconomyEngine;
import pl.psi.map.TurnQueueEconomy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EconomyAIController implements PropertyChangeListener {
    private final BoardEconomyEngine engine;
    private final EconomyHero aiHero;
    private final EconomyAI ai;

    // PAMIĘĆ AI: Przechowuje pozycję, zanim AI zniknie z mapy wchodząc do budynku
    private Point lastKnownPosition;

    public EconomyAIController(BoardEconomyEngine engine, EconomyHero aiHero, EconomyAI ai) {
        this.engine = engine;
        this.aiHero = aiHero;
        this.ai = ai;
        engine.addObserver(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (TurnQueueEconomy.NEXT_HERO.equals(evt.getPropertyName())) {
            if (engine.getCurrentHero() == aiHero) {
                makeMoves();
            }
        }
    }

    private void makeMoves() {
        boolean isTurnActive = true;
        int maxActions = 10; // Mniej akcji, by uniknąć chaotycznego biegania po całej mapie

        while (isTurnActive && engine.getCurrentHero() == aiHero && maxActions > 0) {
            Point currentPos = findHeroPosition();

            // LOGIKA OPUSZCZANIA BUDYNKU:
            if (currentPos != null) {
                lastKnownPosition = currentPos; // Zapisz pozycję dopóki jesteśmy na mapie
            } else if (lastKnownPosition != null) {
                // Zniknęliśmy z mapy (jesteśmy w mieście). Używamy bramy miasta jako punktu startowego!
                currentPos = lastKnownPosition;
            }

            EconomyAction action = ai.decide(engine, aiHero, currentPos);

            if (action instanceof EconomyPassAction) {
                engine.pass();
                isTurnActive = false;
            } else if (action instanceof EconomyMoveAction) {
                Point target = ((EconomyMoveAction) action).getTarget();
                if (engine.canMove(target)) {
                    engine.move(target);
                } else {
                    engine.pass();
                    isTurnActive = false;
                }
            }
            maxActions--;
        }

        if (engine.getCurrentHero() == aiHero) {
            engine.pass();
        }
    }

    private Point findHeroPosition() {
        for (int x = 0; x <= 30; x++) {
            for (int y = 0; y <= 30; y++) {
                Point p = new Point(x, y);
                if (engine.isCurrentHero(p)) {
                    return p;
                }
            }
        }
        return null;
    }
}
