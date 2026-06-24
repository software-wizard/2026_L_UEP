package pl.psi.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.ai.BattleAIRegistrar;
import pl.psi.ai.BattleAI;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.map.BoardEconomyEngine;
import pl.psi.map.MapObjectIf;

import java.util.Map;


@Service
public class GameStateService {
    @Getter
    private GameEngine gameEngine;
    @Getter
    private BoardEconomyEngine boardEconomyEngine;


    public void startBattle(Hero ahero1, Hero ahero2) {
        this.gameEngine = new GameEngine(ahero1, ahero2);
        // automatically register simple Battle AI for hero2 (AI-controlled side)
        try {
            BattleAIRegistrar.register(this.gameEngine, ahero2, new BattleAI());
        } catch (Throwable t) {
            // don't fail startup if AI registration isn't available at runtime
        }
    }

    public void startBoardEconomy(EconomyHero hero1, EconomyHero hero2, Map<Point, MapObjectIf> map) {
        this.boardEconomyEngine = new BoardEconomyEngine(hero1, hero2, map);
        // register economy AI for second hero if available
        try {
            pl.psi.economyAI.EconomyAIRegistrar.register(this.boardEconomyEngine, hero2, new pl.psi.economyAI.EconomyAI());
        } catch (Throwable t) {
            // ignore when economy AI module not present
        }
    }
}
