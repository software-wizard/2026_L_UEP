package pl.psi.gui;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.map.MapObjectIf;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class EconomyBoardController implements PropertyChangeListener {
    private static final String BASE_URL = "http://localhost:8080/api/board";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @FXML private GridPane gridMap;
    @FXML private Button passButton, equipmentButton;
    @FXML private Label goldLabel, attackLabel, defenceLabel, powerLabel, knowledgeLabel;

    public EconomyBoardController(final EconomyHero hero1, final EconomyHero hero2, Map<Point, MapObjectIf> map) {
        try {
            String jsonBody = objectMapper.writeValueAsString(List.of(hero1, hero2));

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/start?mapName=DefaultMap"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        refreshGui();

        passButton.setOnMouseClicked(e -> {
            postAction("/pass", -1, -1);
            refreshGui();
        });

        equipmentButton.setOnMouseClicked(e -> showEquipment());
    }

    private void refreshGui() {
        gridMap.getChildren().clear();

        Map<String, Map<String, Object>> boardState = getBoardState();

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 14; y++) {
                final int currentX = x;
                final int currentY = y;
                String key = x + "," + y;

                Map<String, Object> tileData = boardState.getOrDefault(key, Map.of());
                final EconomyTile mapTile = new EconomyTile("");

                if (Boolean.TRUE.equals(tileData.get("isCurrentHero"))) {
                    mapTile.setBackground(Color.GREENYELLOW);
                } else if (Boolean.TRUE.equals(tileData.get("isHero"))) {
                    mapTile.setBackground(Color.RED);
                }

                if (Boolean.TRUE.equals(tileData.get("hasMapObject"))) {
                    String path = (String) tileData.get("mapObjectPath");
                    if (path != null && !path.isEmpty()) {
                        try {
                            mapTile.setImage("/" + path);
                        } catch (Exception ex) {
                            mapTile.setName("Obj");
                        }
                    }
                }

                if (Boolean.TRUE.equals(tileData.get("canMove"))) {
                    mapTile.setBackground(Color.GREY);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            postAction("/move", currentX, currentY);
                            refreshGui();
                        }
                    });
                }

                if (Boolean.TRUE.equals(tileData.get("canAttack"))) {
                    mapTile.setBackground(Color.INDIANRED);
                }

                if (Boolean.TRUE.equals(tileData.get("canInteract"))) {
                    mapTile.setBackground(Color.YELLOW);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            postAction("/interact", currentX, currentY);
                            refreshGui();
                        }
                    });
                }

                if (Boolean.TRUE.equals(tileData.get("canEnter"))) {
                    mapTile.setBackground(Color.BLUE);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            postAction("/enter", currentX, currentY);
                        } else if (e.getButton() == MouseButton.SECONDARY) {
                            postAction("/secondInteraction", currentX, currentY);
                        }
                        refreshGui();
                    });
                }

                gridMap.add(mapTile, x, y);
            }
        }
        updateResources();
        updateStats();
    }

    private void updateResources() {
        EconomyHero currentHero = getCurrentHero();
        if (currentHero != null && currentHero.getResources() != null) {
            goldLabel.setText("Gold: " + currentHero.getResources().getGold());
        }
    }

    private void updateStats() {
        EconomyHero currentHero = getCurrentHero();
        if (currentHero != null) {
            Statistics stats = currentHero.getTotalStatistics();
            if(stats != null) {
                attackLabel.setText("Attack: " + stats.getAttack());
                defenceLabel.setText("Defense: " + stats.getDefense());
                powerLabel.setText("Power: " + stats.getPower());
                knowledgeLabel.setText("Knowledge: " + stats.getKnowledge());
            }
        }
    }

    private void showEquipment() { /* ... */ }

    private Map<String, Map<String, Object>> getBoardState() {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/boardState")).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                return objectMapper.readValue(res.body(), new com.fasterxml.jackson.core.type.TypeReference<>() {});
            }
        } catch (Exception e) { e.printStackTrace(); }
        return Map.of();
    }

    private void postAction(String endpoint, int x, int y) {
        try {
            String url = x >= 0 ? BASE_URL + endpoint + "?x=" + x + "&y=" + y : BASE_URL + endpoint;
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private EconomyHero getCurrentHero() {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/currentHero")).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                return objectMapper.readValue(res.body(), EconomyHero.class);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshGui();
    }
}