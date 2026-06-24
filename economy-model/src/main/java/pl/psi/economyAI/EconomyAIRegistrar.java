package pl.psi.economyAI;

import pl.psi.hero.EconomyHero;
import pl.psi.map.BoardEconomyEngine;

/**
 * Helper to register EconomyAI for a given BoardEconomyEngine and EconomyHero.
 */
public final class EconomyAIRegistrar {
    private EconomyAIRegistrar() {}

    public static void register(final BoardEconomyEngine engine, final EconomyHero aiHero, final EconomyAI ai) {
        new EconomyAIController(engine, aiHero, ai);
    }
}

