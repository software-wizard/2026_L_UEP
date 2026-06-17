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

    public EconomyAIController(BoardEconomyEngine engine, EconomyHero aiHero, EconomyAI ai) {
        this.engine = engine;
        this.aiHero = aiHero;
        this.ai = ai;
        engine.addObserver(this); // Podpinamy nasłuchiwanie do nienaruszonego silnika
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Gdy zmienia się tura (używamy zmiennej statycznej, z Twojego TurnQueueEconomy)
        if (TurnQueueEconomy.NEXT_HERO.equals(evt.getPropertyName())) {

            // Sprawdzamy czy to w ogóle nasza tura
            if (engine.getCurrentHero() == aiHero) {
                makeMoves();
            }
        }
    }

    private void makeMoves() {
        boolean isTurnActive = true;
        // Zabezpieczenie przed nieskończoną pętlą na wypadek błędu logicznego
        int maxActions = 20;

        while (isTurnActive && engine.getCurrentHero() == aiHero && maxActions > 0) {
            Point currentPos = findHeroPosition();
            EconomyAction action = ai.decide(engine, aiHero, currentPos);

            if (action instanceof EconomyPassAction) {
                engine.pass();
                isTurnActive = false;
            } else if (action instanceof EconomyMoveAction) {
                Point target = ((EconomyMoveAction) action).getTarget();
                if (engine.canMove(target)) {
                    engine.move(target);
                } else {
                    engine.pass(); // Fallback w razie dziwnego błędu
                    isTurnActive = false;
                }
            }
            maxActions--;
        }

        // Jeśli wykorzystaliśmy wszystkie akcje zabezpieczające i wciąż jest tura AI, pasujemy ostatecznie
        if (engine.getCurrentHero() == aiHero) {
            engine.pass();
        }
    }

    // Metoda pomocnicza szukająca bohatera na rozsądnej wielkości planszy
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
