package pl.psi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.map.resources.Resources;
import pl.psi.service.GameStateService;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/board")
public class BoardEconomyController {

    private final GameStateService gameStateService;

    @Autowired
    public BoardEconomyController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

//    @PostMapping("/start")
//    public ResponseEntity<String> startBoardEconomy(@RequestBody List<EconomyHero> heroes) {
//        this.gameStateService.startBoardEconomy(heroes.get(0), heroes.get(1), new HashMap<>());
//        return ResponseEntity.ok("Economy Board engine started successfully.");
//    }
//
//    // Changed to POST because we need to send a Point in the request body
//    @PostMapping("/canMove")
//    public ResponseEntity<Boolean> canMove(@RequestBody Point point) {
//        boolean canMove = this.gameStateService.getBoardEconomyEngine().canMove(point);
//        return ResponseEntity.ok(canMove);
//    }
//
//    @PostMapping("/move")
//    public ResponseEntity<String> move(@RequestBody Point point) {
//        this.gameStateService.getBoardEconomyEngine().move(point);
//        return ResponseEntity.ok("Moved");
//    }
//
//    @GetMapping("/currentHero")
//    public EconomyHero getCurrentHero() {
//        return this.gameStateService.getBoardEconomyEngine().getCurrentHero();
//    }

    @PostMapping("/start")
    public ResponseEntity<String> startBoardEconomy(
            @RequestParam EconomyHero.Fraction fraction1,
            @RequestParam EconomyHero.Fraction fraction2) {

        EconomyHero hero1 = new EconomyHero(fraction1, new Resources(3000, 0, 0, 0, 0, 0, 0), new Statistics(10, 10, 10, 10));
        EconomyHero hero2 = new EconomyHero(fraction2, new Resources(3000, 0, 0, 0, 0, 0, 0), new Statistics(10, 10, 10, 10));

        this.gameStateService.startBoardEconomy(hero1, hero2, new HashMap<>());
        return ResponseEntity.ok("Economy Board engine started successfully.");
    }

    // Uses primitive X and Y coordinates to build the Point locally
    @PostMapping("/canMove")
    public ResponseEntity<Boolean> canMove(@RequestParam int x, @RequestParam int y) {
        boolean canMove = this.gameStateService.getBoardEconomyEngine().canMove(new Point(x, y));
        return ResponseEntity.ok(canMove);
    }

    @PostMapping("/move")
    public ResponseEntity<String> move(@RequestParam int x, @RequestParam int y) {
        this.gameStateService.getBoardEconomyEngine().move(new Point(x, y));
        return ResponseEntity.ok("Moved");
    }

    @GetMapping("/currentHero")
    public EconomyHero getCurrentHero() {
        return this.gameStateService.getBoardEconomyEngine().getCurrentHero();
    }
}