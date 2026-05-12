package pl.psi.gui.proxy;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.gui.BoardEconomyEngineIf;
import pl.psi.map.MapObjectIf;
import pl.psi.map.buildings.BuildingIf;
import pl.psi.map.buildings.town.Town;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BoardEconomyEngineProxy implements BoardEconomyEngineIf {
    private static final String BASE_URL = "http://localhost:8080/api/board";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final PropertyChangeSupport observerSupport = new PropertyChangeSupport(this);

    private Map<String, Map<String, Object>> cachedBoardState = null;

    public BoardEconomyEngineProxy(EconomyHero hero1, EconomyHero hero2, Map<Point, MapObjectIf> map) {
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
    public boolean isEnterable(Point point) {
        return false;
    }

    @Override
    public boolean isHeroAdjacent(Point target) {
        return false;
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
    public boolean canAttack(Point point) {
        return Boolean.TRUE.equals(getTileState(point.getX(), point.getY()).get("canAttack"));
    }

    @Override
    public void move(Point point) {
        postAction("/move", point.getX(), point.getY());
        invalidateCache();
    }

    @Override
    public void interact(Point point) {
        postAction("/interact", point.getX(), point.getY());
        invalidateCache();
    }

    @Override
    public void enter(Point point) {
        postAction("/enter", point.getX(), point.getY());
        invalidateCache();
    }

    @Override
    public void pass() {
        postAction("/pass", -1, -1);
        invalidateCache();
    }

    @Override
    public void secondInteraction(Point point) {
        postAction("/secondInteraction", point.getX(), point.getY());
        invalidateCache();
    }

    @Override
    public EconomyHero getCurrentHero() {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/currentHero")).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && !res.body().isEmpty()) {
                return objectMapper.readValue(res.body(), EconomyHero.class);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public Optional<MapObjectIf> getMapObject(Point point) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/mapObject?x=" + point.getX() + "&y=" + point.getY())).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && !res.body().isEmpty()) {
                return Optional.ofNullable(objectMapper.readValue(res.body(), MapObjectIf.class));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Optional.empty();
    }

    @Override
    public void addObserver(PropertyChangeListener aObserver) {
        observerSupport.addPropertyChangeListener(aObserver);
    }

    @Override
    public Optional<Town> getTownUnderHero(EconomyHero aCurrentHero) {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/townUnderHero"))
                    .GET()
                    .build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() == 200 && !res.body().isEmpty()) {
                return Optional.ofNullable(objectMapper.readValue(res.body(), Town.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void openShop(BuildingIf buildingOpt) {
        observerSupport.firePropertyChange("OPEN_SHOP", null, new Object[]{getCurrentHero(), buildingOpt});
    }

    @Override
    public void openUpgrades(BuildingIf buildingOpt) {
        observerSupport.firePropertyChange("OPEN_UPGRADES", null, new Object[]{getCurrentHero(), buildingOpt});
    }

    @Override
    public void enterBank(BuildingIf building) {
        observerSupport.firePropertyChange("ENTER_BANK", null, new Object[]{getCurrentHero(), building});
    }

    private void postAction(String endpoint, int x, int y) {
        try {
            String url = x >= 0 ? BASE_URL + endpoint + "?x=" + x + "&y=" + y : BASE_URL + endpoint;
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    @Override
    public String getMapObjectPath(Point point) {
        Object path = getTileState(point.getX(), point.getY()).get("mapObjectPath");
        return path != null ? path.toString() : null;
    }
}