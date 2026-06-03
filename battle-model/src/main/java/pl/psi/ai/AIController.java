package pl.psi.ai;

import pl.psi.BattlePoint;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.TurnQueue;
import pl.psi.creatures.Creature;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;

/** Adapter that hooks BattleAI to GameEngine/TurnQueue and executes returned actions. */
public class AIController implements PropertyChangeListener {
    private final GameEngine engine;
    private final Hero aiHero;
    private final BattleAI ai;

    public AIController(final GameEngine engine, final Hero aiHero, final BattleAI ai) {
        this.engine = engine;
        this.aiHero = aiHero;
        this.ai = ai;
        engine.addObserver(this);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (TurnQueue.NEXT_CREATURE.equals(evt.getPropertyName())) {
            if (engine.getCurrentHero() != aiHero) {
                return;
            }

            // find current creature position
            BattlePoint currentPos = null;
            Creature currentCreature = null;
            outer:
            for (int x = 0; x <= 14; x++) {
                for (int y = 0; y <= 14; y++) {
                    BattlePoint p = new BattlePoint(x, y);
                    if (engine.isCurrentCreature(p)) {
                        currentPos = p;
                        currentCreature = engine.getCreature(p).orElse(null);
                        break outer;
                    }
                }
            }

            if (currentPos == null || currentCreature == null) {
                return;
            }

            Optional<Action> decision = ai.decide(currentPos, currentCreature, engine, aiHero);
            if (decision.isEmpty()) {
                return;
            }

            Action action = decision.get();
            if (action instanceof MoveAction) {
                engine.move(((MoveAction) action).getTarget());
            } else if (action instanceof AttackAction) {
                engine.attack(((AttackAction) action).getTarget());
            } else if (action instanceof PassAction) {
                engine.pass();
            }
        }
    }
}

