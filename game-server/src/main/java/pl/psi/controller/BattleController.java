package pl.psi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.psi.BattlePoint;
import pl.psi.Hero;
import pl.psi.SpecialField;
import pl.psi.Spells.Spell;
import pl.psi.creatures.Creature;
import pl.psi.service.GameStateService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/battle")
public class BattleController {

    private final GameStateService gameStateService;

    @Autowired
    public BattleController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startBattle() {
        Hero hero1 = new Hero(List.of(), List.of());
        Hero hero2 = new Hero(List.of(), List.of());

        this.gameStateService.startBattle(hero1, hero2);
        return ResponseEntity.ok("Battle engine started successfully.");
    }

    @PostMapping("/pass")
    public ResponseEntity<String> pass() {
        try {
            this.gameStateService.getGameEngine().pass();
            return ResponseEntity.ok("Turn passed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to pass: " + e.getMessage());
        }
    }

    @GetMapping("/creature")
    public ResponseEntity<Creature> getCreature(@RequestParam int x, @RequestParam int y) {
        try {
            Optional<Creature> creature = this.gameStateService.getGameEngine().getCreature(new BattlePoint(x, y));
            return creature.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/isCurrentCreature")
    public ResponseEntity<Boolean> isCurrentCreature(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getGameEngine().isCurrentCreature(new BattlePoint(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/canMove")
    public ResponseEntity<Boolean> canMove(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getGameEngine().canMove(new BattlePoint(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/move")
    public ResponseEntity<String> move(@RequestParam int x, @RequestParam int y) {
        try {
            this.gameStateService.getGameEngine().move(new BattlePoint(x, y));
            return ResponseEntity.ok("Creature moved.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/canAttack")
    public ResponseEntity<Boolean> canAttack(@RequestParam int x, @RequestParam int y) {
        try {
            return ResponseEntity.ok(this.gameStateService.getGameEngine().canAttack(new BattlePoint(x, y)));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/attack")
    public ResponseEntity<String> attack(@RequestParam int x, @RequestParam int y) {
        try {
            this.gameStateService.getGameEngine().attack(new BattlePoint(x, y));
            return ResponseEntity.ok("Attacked successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/specialField")
    public ResponseEntity<SpecialField> getSpecialField(@RequestParam int x, @RequestParam int y) {
        try {
            SpecialField field = this.gameStateService.getGameEngine().getSpecialFields().get(new BattlePoint(x, y));
            return field != null ? ResponseEntity.ok(field) : ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/interact")
    public ResponseEntity<String> interact(@RequestParam int x, @RequestParam int y) {
        try {
            this.gameStateService.getGameEngine().interact(new BattlePoint(x, y));
            return ResponseEntity.ok("Interacted.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/castSpell")
    public ResponseEntity<String> castSpell(@RequestParam String spellName, @RequestParam int x, @RequestParam int y) {
        try {
            Hero currentHero = this.gameStateService.getGameEngine().getCurrentHero();
            Spell spellToCast = currentHero.getSpells().stream()
                    .filter(s -> s.getName().equals(spellName))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Spell not found"));

            Optional<Creature> target = this.gameStateService.getGameEngine().getCreature(new BattlePoint(x, y));
            if (target.isPresent()) {
                this.gameStateService.getGameEngine().castSpell(spellToCast, target.get());
                return ResponseEntity.ok("Spell casted.");
            }
            return ResponseEntity.badRequest().body("No target.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}