package pl.psi.gui.shops;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.psi.EconomyEngine;
import pl.psi.creatures.EconomyCreature;
import pl.psi.creatures.EconomyNecropolisFactory;
import pl.psi.gui.CreatureButton;
import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.town.CreatureBuildings;
import pl.psi.map.buildings.town.Town;

public class CreatureShopController implements PropertyChangeListener {
    private final EconomyEngine economyEngine;
    private Town town;

    @FXML HBox heroStateHBox;
    @FXML HBox shopsBox;
    @FXML Label playerLabel;
    @FXML Label currentGoldLabel;

    public CreatureShopController(final EconomyHero aHero1, final Town town) {
        this.economyEngine = new EconomyEngine(aHero1);
        this.town = town;
    }

    @FXML
    void initialize() {
        refreshGui();
        economyEngine.addObserver(EconomyEngine.HERO_BOUGHT_CREATURE, this);
    }

    public void refreshGui() {
        playerLabel.setText(economyEngine.getHero().toString());
        currentGoldLabel.setText(String.valueOf(economyEngine.getHero().getResources().getGold()));

        shopsBox.getChildren().clear();
        heroStateHBox.getChildren().clear();

        final EconomyNecropolisFactory factory = new EconomyNecropolisFactory();
        final VBox creatureShopVBox = new VBox(5);

        for (int i = 1; i <= 7; i++) {
            HBox tierHBox = new HBox(10);
            // Przekazujemy miasto i bohatera, aby przycisk znał limity
            tierHBox.getChildren().add(new CreatureButton(this, factory, false, i, town, economyEngine.getHero()));
            tierHBox.getChildren().add(new CreatureButton(this, factory, true, i, town, economyEngine.getHero()));
            creatureShopVBox.getChildren().add(tierHBox);
        }
        shopsBox.getChildren().add(creatureShopVBox);

        // Wyświetlanie aktualnej armii bohatera
        updateHeroState();
    }

    private void updateHeroState() {
        final VBox creaturesBox = new VBox();
        economyEngine.getHero().getCreatures().forEach(c -> {
            creaturesBox.getChildren().add(new Label(c.getAmount() + " x " + c.getName()));
        });
        heroStateHBox.getChildren().add(creaturesBox);
    }

    public void buy(final EconomyCreature aCreature) {
        CreatureBuildings.getBuildingForCreature(aCreature.getStats()).ifPresent(building -> {
            int amountToBuy = aCreature.getAmount();
            // Sprawdzamy dostępność w puli miasta
            if (town.getAvailableUnits(building) >= amountToBuy) {
                economyEngine.buy(aCreature);
                town.buyUnits(building, amountToBuy); // Odejmujemy wybraną ilość
                refreshGui();
            }
        });
    }

    @Override
    public void propertyChange(final PropertyChangeEvent aPropertyChangeEvent) {
        refreshGui();
    }
}