package pl.psi.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import pl.psi.GameEngine;
import pl.psi.Hero;
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
    }

    public void startBoardEconomy(EconomyHero hero1, EconomyHero hero2, Map<Point, MapObjectIf> map) {
        this.boardEconomyEngine = new BoardEconomyEngine(hero1, hero2, map);
    }
}
