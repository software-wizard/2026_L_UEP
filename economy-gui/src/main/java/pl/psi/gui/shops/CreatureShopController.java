package pl.psi.gui.shops;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import lombok.Setter;
import pl.psi.EconomyEngine;
import pl.psi.creatures.EconomyCreature;
import pl.psi.creatures.EconomyNecropolisFactory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.psi.map.buildings.town.CreatureBuildings;
import pl.psi.gui.CreatureButton;
import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.town.Town;

public class CreatureShopController implements PropertyChangeListener
{
    private final EconomyEngine economyEngine;
    @Setter
    private Town town;
    @FXML
    HBox heroStateHBox;
    @FXML
    HBox shopsBox;
    @FXML
    Button readyButton;
    @FXML
    Label playerLabel;
    @FXML
    Label currentGoldLabel;


    public CreatureShopController(final EconomyHero aHero1, final Town town) {
        economyEngine = new EconomyEngine(aHero1);
        this.town = town;
    }


    @FXML
    void initialize()
    {
        refreshGui();
        economyEngine.addObserver( EconomyEngine.HERO_BOUGHT_CREATURE, this );
    }

    public void refreshGui()
    {
        playerLabel.setText( economyEngine.getHero()
            .toString() );
        currentGoldLabel.setText( String.valueOf( economyEngine.getHero()
                .getResources().getGold() ) );
        shopsBox.getChildren()
            .clear();
        heroStateHBox.getChildren()
            .clear();

        final EconomyNecropolisFactory factory = new EconomyNecropolisFactory();
        final VBox creatureShop = new VBox();

        for (int i = 1; i < 8; i++) {
            // 1. Tworzenie instancji jednostek (do pobrania statystyk i kosztów)
            EconomyCreature base = factory.create(false, i, 1);
            EconomyCreature upgraded = factory.create(true, i, 1);

            // 2. Inicjalizacja przycisków
            CreatureButton baseButton = new CreatureButton(this, factory, false, i);
            CreatureButton upgradedButton = new CreatureButton(this, factory, true, i);

            // 3. Obsługa JEDNOSTKI PODSTAWOWEJ
            CreatureBuildings.getBuildingForCreature(base.getStats()).ifPresentOrElse(
                    building -> {
                        int available = town.getAvailableUnits(building); // Pobiera z puli bazy
                        baseButton.setText(base.getName() + " (" + available + ")");

                        // Blokada: brak budynku LUB brak populacji LUB brak złota
                        if (!town.hasBuilt(building) || available <= 0 ||
                                economyEngine.getHero().getResources().getGold() < base.getGoldCost()) {
                            baseButton.setDisable(true);
                        }
                    },
                    () -> baseButton.setDisable(true) // Wyłącz, jeśli nie znaleziono definicji budynku
            );

            // 4. Obsługa JEDNOSTKI ULEPSZONEJ
            CreatureBuildings.getBuildingForCreature(upgraded.getStats()).ifPresentOrElse(
                    upgradedBuilding -> {
                        // KLUCZ: Ulepszona jednostka korzysta z TEJ SAMEJ populacji co podstawa
                        int available = town.getAvailableUnits(upgradedBuilding);
                        upgradedButton.setText(upgraded.getName() + " (" + available + ")");

                        // Blokada dla ulepszonych:
                        // Musi być wybudowane konkretnie ulepszenie (np. Cursed Temple Upgraded)
                        boolean hasBuilding = town.hasBuilt(upgradedBuilding);
                        boolean canAfford = economyEngine.getHero().canAffordGold(upgraded.getGoldCost());

                        if (!hasBuilding || available <= 0 || !canAfford) {
                            upgradedButton.setDisable(true);
                        }
                    },
                    () -> upgradedButton.setDisable(true)
            );

            // 5. Dodanie do kontenera GUI
            creatureShop.getChildren().addAll(baseButton, upgradedButton);
        }
        shopsBox.getChildren().add(creatureShop);


        final VBox creaturesBox = new VBox();
        economyEngine.getHero()
            .getCreatures()
            .forEach( c -> {
                final HBox tempHbox = new HBox();
                tempHbox.getChildren()
                    .add( new Label( String.valueOf( c.getAmount() ) ) );
                tempHbox.getChildren()
                    .add( new Label( c.getName() ) );
                creaturesBox.getChildren()
                    .add( tempHbox );
            } );
        heroStateHBox.getChildren()
            .add( creaturesBox );
    }

    public void buy(final EconomyCreature aCreature) {
        // Znajdź budynek przypisany do tej jednostki
        CreatureBuildings.getBuildingForCreature(aCreature.getStats()).ifPresent(building -> {
            // Sprawdź czy miasto ma te jednostki w puli
            if (town.getAvailableUnits(building) > 0) {
                economyEngine.buy(aCreature);
                town.buyUnits(building, 1); // Zmniejsz pulę o 1
                refreshGui(); // Odśwież widok
            }
        });
    }

    @Override
    public void propertyChange( final PropertyChangeEvent aPropertyChangeEvent )
    {
        refreshGui();
    }
}
