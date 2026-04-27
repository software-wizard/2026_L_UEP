package pl.psi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.artifacts.ArtifactType;
import pl.psi.hero.artifacts.EconomySpell;
import pl.psi.map.MapObjectIf;
import pl.psi.map.buildings.bank.Bank;
import pl.psi.map.buildings.bank.BankStatistics;
import pl.psi.map.buildings.town.Town;
import pl.psi.map.resources.Gold;
import pl.psi.map.resources.Resources;
import pl.psi.map.resources.generators.ResourceGenType;
import pl.psi.map.resources.generators.ResourceGenerator;
import pl.psi.service.GameStateService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/board")
public class BoardEconomyController {

    private static final String SPELL_NAME = "Default";

    private final GameStateService gameStateService;

    @Autowired
    public BoardEconomyController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    private Map<Point, MapObjectIf> createInitialMap() {
        return new HashMap<>(Map.ofEntries(
                Map.entry(new Point(4, 4), new Artifact(ArtifactType.SWORD_OF_HELLFIRE)),
                Map.entry(new Point(17, 1), new Town()),
                Map.entry(new Point(1, 7), new Town()),
                Map.entry(new Point(3, 2), new ResourceGenerator(ResourceGenType.GEM)),
                Map.entry(new Point(5, 6), new ResourceGenerator(ResourceGenType.GOLD)),
                Map.entry(new Point(8, 1), new ResourceGenerator(ResourceGenType.MERCURY)),
                Map.entry(new Point(10, 4), new ResourceGenerator(ResourceGenType.WOOD)),
                Map.entry(new Point(13, 7), new ResourceGenerator(ResourceGenType.SULFUR)),
                Map.entry(new Point(15, 2), new ResourceGenerator(ResourceGenType.CRYSTAL)),
                Map.entry(new Point(6, 8), new ResourceGenerator(ResourceGenType.ORE)),
                Map.entry(new Point(9, 3), new Gold(new Resources(1000, 0, 0, 0, 0, 0, 0))),
                Map.entry(new Point(10, 6), new Gold(new Resources(1000, 0, 0, 0, 0, 0, 0))),
                Map.entry(new Point(2, 2), new Bank(BankStatistics.CASTLE_1)),
                Map.entry(new Point(8, 8), new Bank(BankStatistics.CASTLE_2)),
                Map.entry(new Point(8, 5), new EconomySpell(SPELL_NAME))
        ));
    }

    @PostMapping("/start")
    public ResponseEntity<String> startBoardEconomy(
            @RequestParam EconomyHero.Fraction fraction1,
            @RequestParam EconomyHero.Fraction fraction2) {

        EconomyHero hero1 = new EconomyHero(fraction1, new Resources(3000, 0, 0, 0, 0, 0, 0), new Statistics(10, 10, 10, 10));
        EconomyHero hero2 = new EconomyHero(fraction2, new Resources(3000, 0, 0, 0, 0, 0, 0), new Statistics(10, 10, 10, 10));

        // Inject the generated map here instead of an empty HashMap
        this.gameStateService.startBoardEconomy(hero1, hero2, createInitialMap());

        return ResponseEntity.ok("Economy Board engine started successfully.");
    }

    @PostMapping("/pass")
    public ResponseEntity<String> pass() {
        try {
            this.gameStateService.getBoardEconomyEngine().pass();
            return ResponseEntity.ok("Passed turn.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/currentHero")
    public ResponseEntity<EconomyHero> getCurrentHero() {
        try {
            return ResponseEntity.ok(this.gameStateService.getBoardEconomyEngine().getCurrentHero());
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/isCurrentHero")
    public ResponseEntity<Boolean> isCurrentHero(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getBoardEconomyEngine().isCurrentHero(new Point(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/isHero")
    public ResponseEntity<Boolean> isHero(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getBoardEconomyEngine().isHero(new Point(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/mapObject")
    public ResponseEntity<MapObjectIf> getMapObject(@RequestParam int x, @RequestParam int y) {
        try {
            Optional<MapObjectIf> mapObj = this.gameStateService.getBoardEconomyEngine().getMapObject(new Point(x, y));
            return mapObj.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/canMove")
    public ResponseEntity<Boolean> canMove(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getBoardEconomyEngine().canMove(new Point(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/move")
    public ResponseEntity<String> move(@RequestParam int x, @RequestParam int y) {
        try {
            this.gameStateService.getBoardEconomyEngine().move(new Point(x, y));
            return ResponseEntity.ok("Hero moved.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/canAttack")
    public ResponseEntity<Boolean> canAttack(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getBoardEconomyEngine().canAttack(new Point(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/canInteract")
    public ResponseEntity<Boolean> canInteract(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getBoardEconomyEngine().canInteract(new Point(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/interact")
    public ResponseEntity<String> interact(@RequestParam int x, @RequestParam int y) {
        try {
            this.gameStateService.getBoardEconomyEngine().interact(new Point(x, y));
            return ResponseEntity.ok("Hero interacted.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/canEnter")
    public ResponseEntity<Boolean> canEnter(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getBoardEconomyEngine().canEnter(new Point(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/enter")
    public ResponseEntity<String> enter(@RequestParam int x, @RequestParam int y) {
        try {
            this.gameStateService.getBoardEconomyEngine().enter(new Point(x, y));
            return ResponseEntity.ok("Hero entered.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/secondInteraction")
    public ResponseEntity<String> secondInteraction(@RequestParam int x, @RequestParam int y) {
        try {
            this.gameStateService.getBoardEconomyEngine().secondInteraction(new Point(x, y));
            return ResponseEntity.ok("Hero performed second interaction.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}