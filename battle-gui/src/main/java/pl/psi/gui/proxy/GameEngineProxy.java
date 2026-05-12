package pl.psi.gui.proxy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import pl.psi.BattlePoint;
import pl.psi.gui.GameEngineIf;
import pl.psi.Hero;
import pl.psi.SpecialField;
import pl.psi.creatures.Creature;
import pl.psi.Spells.Spell;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameEngineProxy implements GameEngineIf {
    private static final String BASE_URL = "http://localhost:8080/api/battle";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new GuavaModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    private final PropertyChangeSupport observerSupport = new PropertyChangeSupport(this);

    private Map<String, Map<String, Object>> cachedBoardState = null;

    public GameEngineProxy(Hero aHero1, Hero aHero2, BiMap<BattlePoint, SpecialField> specialFields, Map<BattlePoint, Creature> aBankEnemy) {
        try {
            String jsonBody = objectMapper.writeValueAsString(List.of(aHero1, aHero2));
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/start?mapName=DefaultBattleMap"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                System.err.println("SERVER REJECTED BATTLE START: " + res.body());
            }
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
    public void interact(BattlePoint p) {
        postAction("/interact", p.getX(), p.getY());
        invalidateCache();
        observerSupport.firePropertyChange("REFRESH", null, null);
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
        observerSupport.firePropertyChange("CREATURE_MOVED", null, p);
    }

    @Override
    public void attack(BattlePoint p) {
        postAction("/attack", p.getX(), p.getY());
        invalidateCache();
        observerSupport.firePropertyChange("REFRESH", null, null);
    }

    @Override
    public void pass() {
        postAction("/pass", -1, -1);
        invalidateCache();
        observerSupport.firePropertyChange("REFRESH", null, null);
    }

    @Override
    public Hero getCurrentHero() {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/currentHero")).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && !res.body().isEmpty()) {
                return objectMapper.readValue(res.body(), Hero.class);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Optional<Creature> getCreature(BattlePoint p) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/creature?x=" + p.getX() + "&y=" + p.getY())).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && !res.body().isEmpty()) {
                return Optional.ofNullable(objectMapper.readValue(res.body(), Creature.class));
            }
        } catch (Exception e) { e.printStackTrace(); }
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

                observerSupport.firePropertyChange("SPELL_CAST", null, spell);
            } catch (Exception e) {
                e.printStackTrace();
            }
            invalidateCache();
        }
    }

    @Override
    public void castSpell(Spell spell, BattlePoint targetPoint) {
        try {
            String encodedSpell = spell.getName().replace(" ", "%20");

            String url = BASE_URL + "/castSpell?spellName=" + encodedSpell +
                    "&x=" + targetPoint.getX() +
                    "&y=" + targetPoint.getY();

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            httpClient.send(req, HttpResponse.BodyHandlers.ofString());

            observerSupport.firePropertyChange("SPELL_CAST", null, spell);
        } catch (Exception e) {
            e.printStackTrace();
        }
        invalidateCache();
    }

    @Override
    public void addObserver(PropertyChangeListener aObserver) {
        observerSupport.addPropertyChangeListener(aObserver);
    }

    @Override
    public BiMap<BattlePoint, SpecialField> getSpecialFields() {
        return HashBiMap.create();
    }

    private void postAction(String endpoint, int x, int y) {
        try {
            String url = x >= 0 ? BASE_URL + endpoint + "?x=" + x + "&y=" + y : BASE_URL + endpoint;
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { e.printStackTrace(); }
    }
    @Override
    public boolean isBattleOver() {
        // TODO: You may want to fetch this from your /boardState or a new endpoint.
        // Returning false temporarily to fix compilation.
        return false;
    }

    @Override
    public Optional<pl.psi.BattleResults.BattleResult> getBattleResult() {
        // TODO: Fetch the actual battle result from the server when implemented
        return Optional.empty();
    }
}