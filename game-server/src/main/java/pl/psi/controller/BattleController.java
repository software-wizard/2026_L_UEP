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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/battle")
public class BattleController {

    private final GameStateService gameStateService;

    @Autowired
    public BattleController(GameStateService gameStateService) {
        this.gameStateService = gameStateService;
    }

    @GetMapping("/boardState")
    public ResponseEntity<Map<String, Map<String, Object>>> getBoardState() {
        Map<String, Map<String, Object>> state = new HashMap<>();
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 10; y++) {
                BattlePoint p = new BattlePoint(x, y);
                Map<String, Object> tile = new HashMap<>();

                try {
                    Optional<Creature> c = this.gameStateService.getGameEngine().getCreature(p);
                    if (c.isPresent()) {
                        tile.put("hasCreature", true);
                        tile.put("name", c.get().getName());
                        tile.put("amount", c.get().getAmount());
                    } else {
                        tile.put("hasCreature", false);
                    }

                    SpecialField f = this.gameStateService.getGameEngine().getSpecialFields().get(p);
                    if (f != null) {
                        tile.put("hasSpecialField", true);
                        tile.put("fieldName", f.getFieldName().name());
                        tile.put("fieldColor", f.getColor().name());
                    } else {
                        tile.put("hasSpecialField", false);
                    }

                    tile.put("isCurrentCreature", this.gameStateService.getGameEngine().isCurrentCreature(p));
                    tile.put("canMove", this.gameStateService.getGameEngine().canMove(p));
                    tile.put("canAttack", this.gameStateService.getGameEngine().canAttack(p));

                    state.put(x + "," + y, tile);
                } catch (Exception e) {
                    tile.put("hasCreature", false);
                    tile.put("hasSpecialField", false);
                    tile.put("isCurrentCreature", false);
                    tile.put("canMove", false);
                    tile.put("canAttack", false);
                    state.put(x + "," + y, tile);
                }
            }
        }
        return ResponseEntity.ok(state);
    }

    @PostMapping("/start")
    public ResponseEntity<String> startBattle(
            @RequestBody List<Hero> heroes,
            @RequestParam(defaultValue = "DefaultBattleMap") String mapName) {

        Hero hero1 = heroes.get(0);
        Hero hero2 = heroes.get(1);

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

            this.gameStateService.getGameEngine().castSpell(spellToCast, new BattlePoint(x, y));
            return ResponseEntity.ok("Spell casted.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/currentHero")
    public ResponseEntity<Hero> getCurrentHero() {
        try {
            return ResponseEntity.ok(this.gameStateService.getGameEngine().getCurrentHero());
        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
}