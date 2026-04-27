package pl.psi.gui;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import pl.psi.Hero;
import pl.psi.BattlePoint;
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
import java.util.Map;
import java.util.Optional;

public class MainBattleController implements PropertyChangeListener {
    private static final String BASE_URL = "http://localhost:8080/api/battle";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final SpellCastingManager spellManager = new SpellCastingManager();
    private SpellUIManager spellUIManager;

    @FXML
    private GridPane gridMap;
    @FXML
    private Button passButton;
    @FXML
    private Button spellButton;

    public MainBattleController(final Hero aHero1, final Hero aHero2, final Map<BattlePoint, Creature> bankEnemy, BiMap<BattlePoint, SpecialField> aSpecialField) {
        postAction("/start", -1, -1);
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

        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 10; y++) {
                final int currentX = x;
                final int currentY = y;

                final MapTile mapTile = new MapTile("");

                Optional<SpecialField> specialField = getSpecialField(currentX, currentY);
                Optional<Creature> creature = getCreature(currentX, currentY);

                if (specialField.isPresent()) {
                    mapTile.setBackground(getColor(specialField.get()));
                    mapTile.setName(specialField.get().getFieldName().name());
                }

                if (creature.isPresent()) {
                    mapTile.setName(creature.get().getName() + " x" + creature.get().getAmount());

                    if (getBoolean("/isCurrentCreature", currentX, currentY)) {
                        mapTile.setBackground(Color.GREENYELLOW);
                    }
                }

                if (getBoolean("/canMove", currentX, currentY)) {
                    mapTile.setBackground(Color.GREY);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        postAction("/move", currentX, currentY);
                        refreshGui();
                    });
                }

                if (getBoolean("/canAttack", currentX, currentY)) {
                    mapTile.setBackground(Color.INDIANRED);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        postAction("/attack", currentX, currentY);
                        refreshGui();
                    });
                }

                if (spellManager.isActive() && creature.isPresent()) {
                    mapTile.setBackground(Color.DEEPSKYBLUE);
                    mapTile.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                        spellUIManager.confirmSpellCast(creature.get(), currentX, currentY);
                    });
                }

                gridMap.add(mapTile, x, y);
            }
        }
    }

    private boolean getBoolean(String endpoint, int x, int y) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + endpoint + "?x=" + x + "&y=" + y)).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            return Boolean.parseBoolean(res.body());
        } catch (Exception e) {
            return false;
        }
    }

    private void postAction(String endpoint, int x, int y) {
        try {
            String url = x >= 0 ? BASE_URL + endpoint + "?x=" + x + "&y=" + y : BASE_URL + endpoint;
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Optional<Creature> getCreature(int x, int y) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/creature?x=" + x + "&y=" + y)).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && res.body() != null && !res.body().isEmpty()) {
                return Optional.of(objectMapper.readValue(res.body(), Creature.class));
            }
        } catch (Exception e) { }
        return Optional.empty();
    }

    private Optional<SpecialField> getSpecialField(int x, int y) {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/specialField?x=" + x + "&y=" + y)).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && res.body() != null && !res.body().isEmpty()) {
                return Optional.of(objectMapper.readValue(res.body(), SpecialField.class));
            }
        } catch (Exception e) { }
        return Optional.empty();
    }

    private Color getColor(SpecialField specialField) {
        if (specialField == null || specialField.getColor() == null) return Color.WHITE;
        switch (specialField.getColor()) {
            case BROWN: return Color.BROWN;
            case CYAN: return Color.CYAN;
            case YELLOW: return Color.YELLOW;
            case ORANGE: return Color.ORANGE;
            case GRAY: return Color.GRAY;
            case RED: return Color.RED;
            default: return Color.WHITE;
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshGui();
    }
}