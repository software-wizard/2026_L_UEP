package pl.psi.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.psi.creatures.EconomyCreature;
import pl.psi.creatures.EconomyNecropolisFactory;
import pl.psi.gui.shops.CreatureShopController;
import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.town.CreatureBuildings;
import pl.psi.map.buildings.town.Town;

public class CreatureButton extends Button {
    private final String creatureName;
    private final int unitCost;
    private final int maxAvailable;
    private Stage dialog;

    public CreatureButton(final CreatureShopController aController, final EconomyNecropolisFactory aFactory,
                          final boolean aUpgraded, final int aTier, final Town aTown, final EconomyHero aHero) {

        // Tworzymy próbkę jednostki, by pobrać statystyki
        EconomyCreature sample = aFactory.create(aUpgraded, aTier, 1);
        this.creatureName = sample.getName();
        this.unitCost = sample.getGoldCost();

        // Obliczamy limity
        CreatureBuildings building = CreatureBuildings.getBuildingForCreature(sample.getStats()).orElse(null);
        int townPopulation = (building != null) ? aTown.getAvailableUnits(building) : 0;
        int affordable = aHero.getResources().getGold() / Math.max(1, unitCost);
        this.maxAvailable = Math.min(townPopulation, affordable);

        this.setText(creatureName + " (" + townPopulation + ")");

        // Blokada przycisku: brak budynku lub brak jednostek/złota
        this.setDisable(building == null || !aTown.hasBuilt(building) || maxAvailable <= 0);

        this.setOnAction(e -> {
            final int selectedAmount = startDialogAndGetCreatureAmount();
            if (selectedAmount > 0) {
                // Tworzymy finalny stos o wybranej ilości
                aController.buy(aFactory.create(aUpgraded, aTier, selectedAmount));
            }
        });
    }

    private int startDialogAndGetCreatureAmount() {
        VBox centerPane = new VBox(10);
        HBox bottomPane = new HBox(10);
        HBox topPane = new HBox(10);

        this.dialog = prepareWindow(centerPane, bottomPane, topPane);
        Slider slider = createSlider(maxAvailable);

        Label totalCostLabel = new Label("Total Cost: 0");
        Label currentAmountLabel = new Label("Amount: 0");

        slider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int amount = newVal.intValue();
            currentAmountLabel.setText("Amount: " + amount);
            totalCostLabel.setText("Total Cost: " + (amount * unitCost));
        });

        topPane.getChildren().addAll(new Label("Cost: " + unitCost), currentAmountLabel, totalCostLabel);
        centerPane.getChildren().add(slider);

        prepareButtons(bottomPane, slider);

        dialog.showAndWait();
        return (int) slider.getValue();
    }

    private Slider createSlider(int max) {
        Slider slider = new Slider(0, max, 0);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(Math.max(1, max / 5));
        slider.setBlockIncrement(1);
        slider.setSnapToTicks(true);
        return slider;
    }

    private void prepareButtons(HBox bottom, Slider slider) {
        Button okBtn = new Button("OK");
        okBtn.setOnAction(e -> dialog.close());

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(e -> {
            slider.setValue(0);
            dialog.close();
        });

        bottom.setAlignment(Pos.CENTER);
        bottom.getChildren().addAll(okBtn, cancelBtn);
    }

    private Stage prepareWindow(Pane center, Pane bottom, Pane top) {
        Stage stage = new Stage();
        BorderPane root = new BorderPane();
        root.setPadding(new javafx.geometry.Insets(15));
        root.setTop(top);
        root.setCenter(center);
        root.setBottom(bottom);

        stage.setScene(new Scene(root, 400, 250));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Recruiting " + creatureName);
        return stage;
    }
}