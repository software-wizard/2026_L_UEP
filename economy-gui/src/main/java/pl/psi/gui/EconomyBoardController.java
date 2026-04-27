package pl.psi.gui;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.Statistics;
import pl.psi.map.MapObjectIf;
import pl.psi.economy.Point;
import pl.psi.map.resources.Resources;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Optional;

public class EconomyBoardController implements PropertyChangeListener {
    private static final String BASE_URL = "http://localhost:8080/api/board";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @FXML private GridPane gridMap;
    @FXML private Button passButton, equipmentButton;
    @FXML private Label goldLabel, woodLabel, oreLabel, mercuryLabel, sulphurLabel, crystalLabel, gemsLabel, attackLabel, defenceLabel, powerLabel, knowledgeLabel;

    public EconomyBoardController(final EconomyHero hero1, final EconomyHero hero2, Map<Point, MapObjectIf> map) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/start?fraction1=NECROPOLIS&fraction2=NECROPOLIS"))
                    .POST(HttpRequest.BodyPublishers.noBody()).build();
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
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 14; y++) {
                final int currentX = x;
                final int currentY = y;

                final EconomyTile mapTile = new EconomyTile("");

                if (getBoolean("/isCurrentHero", currentX, currentY)) {
                    mapTile.setBackground(Color.GREENYELLOW);
                } else if (getBoolean("/isHero", currentX, currentY)) {
                    mapTile.setBackground(Color.RED);
                }

                Optional<MapObjectIf> mapObject = getMapObject(currentX, currentY);
                if (mapObject.isPresent()) {
                    mapTile.setImage(mapObject.get().getPath());
                }

                if (getBoolean("/canMove", currentX, currentY)) {
                    mapTile.setBackground(Color.GREY);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            postAction("/move", currentX, currentY);
                            refreshGui();
                        }
                    });
                }

                if (getBoolean("/canAttack", currentX, currentY)) {
                    mapTile.setBackground(Color.INDIANRED);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        // Handle attack logic
                    });
                }

                if (getBoolean("/canInteract", currentX, currentY)) {
                    mapTile.setBackground(Color.YELLOW);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        if (e.getButton() == MouseButton.PRIMARY) {
                            postAction("/interact", currentX, currentY);
                            refreshGui();
                        }
                    });
                }

                if (getBoolean("/canEnter", currentX, currentY)) {
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
        if (currentHero == null) return;

        Resources res = currentHero.getResources();
        goldLabel.setText("Gold: " + res.getGold());
        woodLabel.setText("Wood: " + res.getWood());
        oreLabel.setText("Ore: " + res.getOre());
        mercuryLabel.setText("Mercury: " + res.getMercury());
        sulphurLabel.setText("Sulphur: " + res.getSulphur());
        crystalLabel.setText("Crystal: " + res.getCrystal());
        gemsLabel.setText("Gems: " + res.getGems());
    }

    private void updateStats() {
        EconomyHero currentHero = getCurrentHero();
        if (currentHero == null) return;

        Statistics stats = currentHero.getTotalStatistics();
        attackLabel.setText("Attack: " + stats.getAttack());
        defenceLabel.setText("Defense: " + stats.getDefense());
        powerLabel.setText("Power: " + stats.getPower());
        knowledgeLabel.setText("Knowledge: " + stats.getKnowledge());
    }

    private void showEquipment() {
        WindowManager.openEquipment(getCurrentHero());
    }


    private boolean getBoolean(String endpoint, int x, int y) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + endpoint + "?x=" + x + "&y=" + y)).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(res.body());
        } catch (Exception e) { return false; }
    }

    private void postAction(String endpoint, int x, int y) {
        try {
            String url = x >= 0 ? BASE_URL + endpoint + "?x=" + x + "&y=" + y : BASE_URL + endpoint;
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) { e.printStackTrace(); }
    }

    private Optional<MapObjectIf> getMapObject(int x, int y) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/mapObject?x=" + x + "&y=" + y)).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && res.body() != null && !res.body().isEmpty()) {
                return Optional.of(objectMapper.readValue(res.body(), MapObjectIf.class));
            }
        } catch (Exception e) { }
        return Optional.empty();
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