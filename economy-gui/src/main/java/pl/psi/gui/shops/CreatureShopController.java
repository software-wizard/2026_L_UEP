package pl.psi.gui.shops;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import pl.psi.creatures.EconomyCreature;
import pl.psi.creatures.EconomyNecropolisFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.psi.map.buildings.town.UpgradeBuildings;
import pl.psi.gui.CreatureButton;
import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.town.Town;

public class CreatureShopController implements PropertyChangeListener {

    private static final String BASE_URL = "http://localhost:8080/api/economy";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Setter
    private Town town;

    @FXML HBox heroStateHBox;
    @FXML HBox shopsBox;
    @FXML Button readyButton;
    @FXML Label playerLabel;
    @FXML Label currentGoldLabel;

    public CreatureShopController(final EconomyHero aHero1, final Town town) {
        this.town = town;
    }

    @FXML
    void initialize() {
        refreshGui();
    }

    public void refreshGui() {
        EconomyHero currentHero = getCurrentHero();
        if (currentHero == null) return;

        playerLabel.setText("Player ");
        currentGoldLabel.setText(String.valueOf(currentHero.getResources().getGold() ));

        heroStateHBox.getChildren().clear();
        shopsBox.getChildren().clear();

        final EconomyNecropolisFactory factory = new EconomyNecropolisFactory();

        for (int i = 1; i < 8; i++) {
            final EconomyCreature base = factory.create(false, i, 1);
            final EconomyCreature upgraded = factory.create(true, i, 1);

            final VBox creatureShop = new VBox();
            final CreatureButton baseButton = new CreatureButton(this,factory, false, i);
            final CreatureButton upgradedButton = new CreatureButton(this, factory, false, i);

            // Logic to disable buttons based on Town upgrades
            UpgradeBuildings.getBuildingForCreature(base.getStats())
                    .ifPresentOrElse(
                            building -> {
                                if (!town.hasBuilt(building)) baseButton.setDisable(true);
                            },
                            () -> baseButton.setDisable(true)
                    );

            UpgradeBuildings.getBuildingForCreature(upgraded.getStats())
                    .ifPresentOrElse(
                            building -> {
                                if (!town.hasBuilt(building)) upgradedButton.setDisable(true);
                            },
                            () -> upgradedButton.setDisable(true)
                    );

            creatureShop.getChildren().addAll(baseButton, upgradedButton);
            shopsBox.getChildren().add(creatureShop);
        }

        final VBox creaturesBox = new VBox();
        currentHero.getCreatures().forEach(c -> {
            final HBox tempHbox = new HBox();
            tempHbox.getChildren().add(new Label(String.valueOf(c.getAmount())));
            tempHbox.getChildren().add(new Label(c.getName()));
            creaturesBox.getChildren().add(tempHbox);
        });

        heroStateHBox.getChildren().add(creaturesBox);
    }

    public void buy(final EconomyCreature aCreature) {
        // We use the REST API query params as designed previously
        try {
            boolean isUpgraded = aCreature.isUpgraded();
            int tier = aCreature.getTier();
            int amount = aCreature.getAmount();

            String url = BASE_URL + "/buy?upgraded=" + isUpgraded + "&tier=" + tier + "&amount=" + amount;
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).POST(HttpRequest.BodyPublishers.noBody()).build();
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            refreshGui(); // Reload the UI after purchase
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EconomyHero getCurrentHero() {
        try {
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/hero")).GET().build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() == 200) {
                return objectMapper.readValue(res.body(), EconomyHero.class);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent aPropertyChangeEvent) {
        refreshGui();
    }
}