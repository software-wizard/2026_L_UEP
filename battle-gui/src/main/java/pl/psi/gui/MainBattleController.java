package pl.psi.gui;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import pl.psi.BattlePoint;
import pl.psi.Hero;
import pl.psi.SpecialField;
import pl.psi.creatures.Creature;
import pl.psi.gui.SpellGUI.SpellCastingManager;
import pl.psi.gui.SpellGUI.SpellUIManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class MainBattleController implements PropertyChangeListener {
    private static final String BASE_URL = "http://localhost:8080/api/battle";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final SpellCastingManager spellManager = new SpellCastingManager();
    private SpellUIManager spellUIManager;

    @FXML private GridPane gridMap;
    @FXML private Button passButton;
    @FXML private Button spellButton;

    public MainBattleController(final Hero aHero1, final Hero aHero2, final Map<BattlePoint, Creature> bankEnemy, BiMap<BattlePoint, SpecialField> aSpecialField) {
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

    @FXML
    private void initialize() {
        spellUIManager = new SpellUIManager(spellManager, this::refreshGui);
        refreshGui();

        passButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            postAction("/pass", -1, -1);
            refreshGui();
        });

        spellButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (spellUIManager != null) {
                spellUIManager.openSpellDialog();
            }
        });
    }

    private void refreshGui() {
        gridMap.getChildren().clear();

        Map<String, Map<String, Object>> boardState = getBoardState();

        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 10; y++) {
                final int currentX = x;
                final int currentY = y;
                String key = x + "," + y;

                Map<String, Object> tileData = boardState.getOrDefault(key, Map.of());
                final MapTile mapTile = new MapTile("");

                if (Boolean.TRUE.equals(tileData.get("hasSpecialField"))) {
                    mapTile.setBackground(getColorFromString((String) tileData.get("fieldColor")));
                    mapTile.setName((String) tileData.get("fieldName"));
                }

                if (Boolean.TRUE.equals(tileData.get("hasCreature"))) {
                    mapTile.setName(tileData.get("name") + " x" + tileData.get("amount"));
                    if (Boolean.TRUE.equals(tileData.get("isCurrentCreature"))) {
                        mapTile.setBackground(Color.GREENYELLOW);
                    }
                }

                if (Boolean.TRUE.equals(tileData.get("canMove"))) {
                    mapTile.setBackground(Color.GREY);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        postAction("/move", currentX, currentY);
                        refreshGui();
                    });
                }

                if (Boolean.TRUE.equals(tileData.get("canAttack"))) {
                    mapTile.setBackground(Color.INDIANRED);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        postAction("/attack", currentX, currentY);
                        refreshGui();
                    });
                }

                gridMap.add(mapTile, x, y);
            }
        }
    }


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

    private Color getColorFromString(String colorStr) {
        if (colorStr == null) return Color.WHITE;
        switch (colorStr) {
            case "BROWN": return Color.BROWN;
            case "CYAN": return Color.CYAN;
            case "YELLOW": return Color.YELLOW;
            case "ORANGE": return Color.ORANGE;
            case "GRAY": return Color.GRAY;
            case "RED": return Color.RED;
            default: return Color.WHITE;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshGui();
    }
}