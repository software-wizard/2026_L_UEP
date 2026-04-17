package pl.psi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.psi.service.GameStateService;
import java.util.List;
import pl.psi.Hero;

@RestController
@RequestMapping("/api/battle")
public class BattleController {

    private final GameStateService gameStateService;

    @Autowired
    public BattleController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startBattle(@RequestBody List<Hero> heroes) {
        this.gameStateService.startBattle(heroes.get(0), heroes.get(1));
        return ResponseEntity.ok("Battle engine started successfully.");
    }

    @PostMapping("/pass")
    public ResponseEntity<String> pass() {
        this.gameStateService.getGameEngine().pass();
        return ResponseEntity.ok("Turn passed");
    }
}