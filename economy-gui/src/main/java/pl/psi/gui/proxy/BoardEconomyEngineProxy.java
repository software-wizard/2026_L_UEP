package pl.psi.gui.proxy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.map.BoardEconomyEngine;
import pl.psi.map.MapObjectIf;
import pl.psi.map.buildings.enterAction.EnterAction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BoardEconomyEngineProxy extends BoardEconomyEngine {
    private static final String BASE_URL = "http://localhost:8080/api/board";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private Map<String, Map<String, Object>> cachedBoardState = null;

    public BoardEconomyEngineProxy(EconomyHero hero1, EconomyHero hero2, Map<Point, MapObjectIf> map) {
        super(hero1, hero2, map);
        try {
            String jsonBody = objectMapper.writeValueAsString(List.of(hero1, hero2));
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/start?mapName=DefaultMap"))
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
    public boolean isCurrentHero(Point point) {
        return Boolean.TRUE.equals(getTileState(point.getX(), point.getY()).get("isCurrentHero"));
    }

    @Override
    public boolean isHero(Point point) {
        return Boolean.TRUE.equals(getTileState(point.getX(), point.getY()).get("isHero"));
    }

    @Override
    public boolean canMove(Point point) {
        return Boolean.TRUE.equals(getTileState(point.getX(), point.getY()).get("canMove"));
    }

    @Override
    public boolean canInteract(Point point) {
        return Boolean.TRUE.equals(getTileState(point.getX(), point.getY()).get("canInteract"));
    }

    @Override
    public boolean canEnter(Point point) {
        return Boolean.TRUE.equals(getTileState(point.getX(), point.getY()).get("canEnter"));
    }

    @Override
    public void move(Point point) {
        postAction("/move", point.getX(), point.getY());
        invalidateCache();
        super.move(point);
    }

    @Override
    public void interact(Point point) {
        postAction("/interact", point.getX(), point.getY());
        invalidateCache();
        super.interact(point);
    }

    @Override
    public void enter(Point point) {
        postAction("/enter", point.getX(), point.getY());
        invalidateCache();
        super.enter(point);
    }

    @Override
    public void pass() {
        postAction("/pass", -1, -1);
        invalidateCache();
        super.pass();
    }

    @Override
    public void secondInteraction(Point point) {
        postAction("/secondInteraction", point.getX(), point.getY());
        invalidateCache();
        super.secondInteraction(point);
    }

    @Override
    public EconomyHero getCurrentHero() {
        return super.getCurrentHero();
    }

    @Override
    public Optional<MapObjectIf> getMapObject(Point point) {
        Map<String, Object> tile = getTileState(point.getX(), point.getY());
        if (Boolean.TRUE.equals(tile.get("hasMapObject"))) {
            Optional<MapObjectIf> localObj = super.getMapObject(point);
            if (localObj.isPresent()) {
                return localObj;
            }
            return Optional.of(new MapObjectIf() {
                @Override
                public String getPath() {
                    String path = (String) tile.get("mapObjectPath");
                    return (path != null && !path.startsWith("/")) ? "/" + path : path;
                }
                @Override public void endOfTurn() {}
                @Override public void generateResource() {}
                @Override public void generateUnits() { }
                @Override public void interact(EconomyHero hero) {}
                @Override public typeOfObject getTypeOfObject() { return null; }
                @Override public EconomyHero getOwner() { return null; }

                @Override
                public EnterAction firstInteraction() {
                    return null;
                }

                @Override public EnterAction secondInteraction() { return null; }

                @Override
                public void resetBuildingOption() {

                }
            });
        }
        return Optional.empty();
    }

    private void postAction(String endpoint, int x, int y) {
        try {
            String url = x >= 0 ? BASE_URL + endpoint + "?x=" + x + "&y=" + y : BASE_URL + endpoint;
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { e.printStackTrace(); }
    }
}