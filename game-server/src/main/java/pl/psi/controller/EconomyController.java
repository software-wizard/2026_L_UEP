package pl.psi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.psi.creatures.EconomyNecropolisFactory;
import pl.psi.hero.Statistics;
import pl.psi.map.resources.Resources;
import pl.psi.service.*;
import pl.psi.hero.EconomyHero;
import pl.psi.creatures.EconomyCreature;

@RestController
@RequestMapping("/api/economy")
public class EconomyController {

    private final GameStateService gameStateService;

    @Autowired
    public EconomyController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

//    @PostMapping("/start")
//    public ResponseEntity<String> startEconomy(@RequestBody EconomyHero aHero) {
//        this.gameStateService.startEconomy(aHero);
//        return ResponseEntity.ok("Economy engine started successfully.");
//    }
//    @GetMapping("/hero")
//    public EconomyHero getHero() {
//        return this.gameStateService.getEconomyEngine().getHero();
//    }
//
//    @PostMapping("/buy")
//    public ResponseEntity<String> buyCreature(@RequestBody EconomyCreature aEconomyCreature) {
//        this.gameStateService.getEconomyEngine().buy(aEconomyCreature);
//        return ResponseEntity.ok("Creature purchased successfully.");
//    }


    @PostMapping("/start")
    public ResponseEntity<String> startEconomy(@RequestParam EconomyHero.Fraction fraction) {

        EconomyHero startingHero = new EconomyHero(fraction, new Resources(1000, 0, 0, 0, 0, 0, 0), new Statistics(10, 10, 10, 10));

        this.gameStateService.startEconomy(startingHero);
        return ResponseEntity.ok("Economy engine started successfully.");
    }
    @GetMapping("/hero")
    public EconomyHero getHero() {
        return this.gameStateService.getEconomyEngine().getHero();
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyCreature(
            @RequestParam boolean upgraded,
            @RequestParam int tier,
            @RequestParam int amount) {

        EconomyNecropolisFactory factory = new EconomyNecropolisFactory();
        EconomyCreature creature = factory.create(upgraded, tier, amount);

        this.gameStateService.getEconomyEngine().buy(creature);
        return ResponseEntity.ok("Creature purchased successfully.");
    }
}