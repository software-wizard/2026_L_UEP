package pl.psi.ai;

import pl.psi.GameEngine;
import pl.psi.Hero;

/**
 * Helper to register BattleAI for a given GameEngine and Hero (AI-controlled).
 * This class is non-invasive and can be called from the launcher without
 * modifying existing engine code.
 */
public final class BattleAIRegistrar {
    private BattleAIRegistrar() {}

    public static void register(final GameEngine engine, final Hero aiHero, final BattleAI ai) {
        new AIController(engine, aiHero, ai);
    }
}

