package pl.psi.gui.proxy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import pl.psi.BattlePoint;
import pl.psi.GameEngine;
import pl.psi.Hero;
import pl.psi.SpecialField;
import pl.psi.creatures.Creature;
import pl.psi.Spells.Spell;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameEngineProxy extends GameEngine {
    private static final String BASE_URL = "http://localhost:8080/api/battle";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Map<String, Map<String, Object>> cachedBoardState = null;

    public GameEngineProxy(Hero aHero1, Hero aHero2, BiMap<BattlePoint, SpecialField> specialFields, Map<BattlePoint, Creature> aBankEnemy) {
        super(aHero1, aHero2, specialFields, aBankEnemy);
        try {
            String jsonBody = objectMapper.writeValueAsString(List.of(aHero1, aHero2));
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/start?mapName=DefaultBattleMap"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private Map<String, Object> getTileState(int x, int y) {
        if (cachedBoardState == null) {
            try {
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/boardState")).GET().build();
                HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
                if (res.statusCode() == 200) {
                    cachedBoardState = objectMapper.readValue(res.body(), new com.fasterxml.jackson.core.type.TypeReference<>() {});
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
        if (cachedBoardState != null) {
            return cachedBoardState.getOrDefault(x + "," + y, Map.of());
        }
        return Map.of();
    }

    private void invalidateCache() {
        cachedBoardState = null;
    }

    @Override
    public boolean isCurrentCreature(BattlePoint p) {
        return Boolean.TRUE.equals(getTileState(p.getX(), p.getY()).get("isCurrentCreature"));
    }

    @Override
    public boolean canMove(BattlePoint p) {
        return Boolean.TRUE.equals(getTileState(p.getX(), p.getY()).get("canMove"));
    }

    @Override
    public boolean canAttack(BattlePoint p) {
        return Boolean.TRUE.equals(getTileState(p.getX(), p.getY()).get("canAttack"));
    }

    @Override
    public void move(BattlePoint p) {
        postAction("/move", p.getX(), p.getY());
        invalidateCache();
        super.move(p);
    }

    @Override
    public void attack(BattlePoint p) {
        postAction("/attack", p.getX(), p.getY());
        invalidateCache();
        super.attack(p);
    }

    @Override
    public void pass() {
        postAction("/pass", -1, -1);
        invalidateCache();
        super.pass();
    }

    @Override
    public Hero getCurrentHero() {
        return super.getCurrentHero();
    }

    @Override
    public Optional<Creature> getCreature(BattlePoint p) {
        Map<String, Object> tile = getTileState(p.getX(), p.getY());
        if (Boolean.TRUE.equals(tile.get("hasCreature"))) {
            return super.getCreature(p);
        }
        return Optional.empty();
    }

    @Override
    public void castSpell(Spell spell, Creature targetCreature) {
        int targetX = -1;
        int targetY = -1;

        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 10; y++) {
                Optional<Creature> c = getCreature(new BattlePoint(x, y));
                if (c.isPresent() && c.get().getName().equals(targetCreature.getName())) {
                    targetX = x;
                    targetY = y;
                    break;
                }
            }
        }

        if (targetX != -1) {
            try {
                String encodedSpell = spell.getName().replace(" ", "%20");
                String url = BASE_URL + "/castSpell?spellName=" + encodedSpell + "&x=" + targetX + "&y=" + targetY;
                HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
                httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            invalidateCache();
        }
        super.castSpell(spell, targetCreature);
    }

    private void postAction(String endpoint, int x, int y) {
        try {
            String url = x >= 0 ? BASE_URL + endpoint + "?x=" + x + "&y=" + y : BASE_URL + endpoint;
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { e.printStackTrace(); }
    }
}