package pl.psi.service;

import java.util.Map;

import lombok.Getter;
import org.springframework.stereotype.Service;
import pl.psi.EconomyEngine;
import pl.psi.economy.Point;
import pl.psi.map.BoardEconomyEngine;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.hero.EconomyHero;
import pl.psi.map.MapObjectIf;


@Service
public class GameStateService {
    @Getter
    private GameEngine gameEngine;
    @Getter
    private EconomyEngine economyEngine;
    @Getter
    private BoardEconomyEngine boardEconomyEngine;


    public void startBattle(Hero ahero1, Hero ahero2) {
        this.gameEngine = new GameEngine(ahero1,ahero2);
    }

    public void startEconomy(EconomyHero ahero1) {
        this.economyEngine = new EconomyEngine(ahero1);
    }

    public void startBoardEconomy(EconomyHero hero1, EconomyHero hero2, Map<Point, MapObjectIf> map) {
        this.boardEconomyEngine = new BoardEconomyEngine(hero1, hero2, map);
    }
}
