package pl.psi.gui.SpellGUI;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.psi.Hero;
import pl.psi.Spells.Spell;
import pl.psi.creatures.Creature;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpellUIManager {

    private static final String BASE_URL = "http://localhost:8080/api/battle";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final SpellCastingManager spellManager;
    private final Runnable guiRefresher;

    public SpellUIManager(SpellCastingManager spellManager, Runnable guiRefresher) {
        this.spellManager = spellManager;
        this.guiRefresher = guiRefresher;
    }

    public void openSpellDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/spell-dialog.fxml"));
            VBox dialogRoot = loader.load();
            SpellDialogController controller = loader.getController();
            Hero currentHero = getCurrentHero();

            if (currentHero != null) {
                controller.setSpells(currentHero.getSpells(), spell -> {
                    spellManager.activate(spell);
                    guiRefresher.run();
                });
            }

            Stage dialogStage = new Stage();
            dialogStage.setScene(new Scene(dialogRoot));
            dialogStage.setTitle("Spells");
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void confirmSpellCast(Creature creature, int x, int y) {
        Spell selectedSpell = spellManager.getSelectedSpell();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie zaklęcia");
        alert.setHeaderText("Rzucić zaklęcie " + selectedSpell.getName() + " na " + creature.getName() + "?");

        ButtonType okButton = new ButtonType("Tak", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Nie", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                castSpell(selectedSpell.getName(), x, y);
                showSpellCastDialog();
            }
            spellManager.deactivate();
            guiRefresher.run();
        });
    }

    public void showSpellCastDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Zaklęcie rzucone");
        alert.setHeaderText(null);
        alert.setContentText("Zaklęcie zostało pomyślnie rzucone!");
        alert.showAndWait();
    }

    private Hero getCurrentHero() {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/currentHero")).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200 && res.body() != null && !res.body().isEmpty()) {
                return objectMapper.readValue(res.body(), Hero.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void castSpell(String spellName, int x, int y) {
        try {
            String encodedSpell = spellName.replace(" ", "%20");
            String url = BASE_URL + "/castSpell?spellName=" + encodedSpell + "&x=" + x + "&y=" + y;

            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}