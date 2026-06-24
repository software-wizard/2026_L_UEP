package pl.psi.gui.shops;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import lombok.Setter;
import pl.psi.EconomyEngine;
import pl.psi.creatures.CreatureStatistic;
import pl.psi.creatures.EconomyCreature;
import pl.psi.creatures.EconomyBastionFactory;
import pl.psi.creatures.EconomyNecropolisFactory;
import pl.psi.hero.EconomyHero;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pl.psi.map.buildings.town.BastionUpgradeBuildings;
import pl.psi.map.buildings.town.UpgradeBuildings;
import pl.psi.gui.CreatureButton;
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

    // Explicit hardcoded creature -> dwelling mapping for Bastion.
    // No stream/lambda matching, no ambiguity — just a direct table.
    private static final Map<CreatureStatistic, BastionUpgradeBuildings> BASTION_BASE_DWELLING = new HashMap<>();
    private static final Map<CreatureStatistic, BastionUpgradeBuildings> BASTION_UPGRADED_DWELLING = new HashMap<>();
    static {
        BASTION_BASE_DWELLING.put(CreatureStatistic.CENTAUR, BastionUpgradeBuildings.CENTAUR_STABLES);
        BASTION_UPGRADED_DWELLING.put(CreatureStatistic.BATTLE_CENTAUR, BastionUpgradeBuildings.CENTAUR_STABLES_UPGRADED);

        BASTION_BASE_DWELLING.put(CreatureStatistic.DWARF, BastionUpgradeBuildings.DWARF_COTTAGE);
        BASTION_UPGRADED_DWELLING.put(CreatureStatistic.DWARF_WARRIOR, BastionUpgradeBuildings.DWARF_COTTAGE_UPGRADED);

        BASTION_BASE_DWELLING.put(CreatureStatistic.ELF, BastionUpgradeBuildings.HOMESTEAD);
        BASTION_UPGRADED_DWELLING.put(CreatureStatistic.HIGH_ELF, BastionUpgradeBuildings.HOMESTEAD_UPGRADED);

        BASTION_BASE_DWELLING.put(CreatureStatistic.PEGASUS, BastionUpgradeBuildings.PEGASUS_NEST);
        BASTION_UPGRADED_DWELLING.put(CreatureStatistic.SILVER_PEGASUS, BastionUpgradeBuildings.PEGASUS_NEST_UPGRADED);

        BASTION_BASE_DWELLING.put(CreatureStatistic.TREEMAN, BastionUpgradeBuildings.DENDROID_ARCHES);
        BASTION_UPGRADED_DWELLING.put(CreatureStatistic.ENT, BastionUpgradeBuildings.DENDROID_ARCHES_UPGRADED);

        BASTION_BASE_DWELLING.put(CreatureStatistic.UNICORN, BastionUpgradeBuildings.UNICORN_GLADE);
        BASTION_UPGRADED_DWELLING.put(CreatureStatistic.BATTLE_UNICORN, BastionUpgradeBuildings.UNICORN_GLADE_UPGRADED);

        BASTION_BASE_DWELLING.put(CreatureStatistic.GREEN_DRAGON, BastionUpgradeBuildings.DRAGON_CLIFFS);
        BASTION_UPGRADED_DWELLING.put(CreatureStatistic.GOLD_DRAGON, BastionUpgradeBuildings.DRAGON_CLIFFS_UPGRADED);
    }


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

        final VBox creatureShop = new VBox();

        if (economyEngine.getHero().getFraction() == EconomyHero.Fraction.BASTION) {
            final EconomyBastionFactory factory = new EconomyBastionFactory();

            for (int i = 1; i < 8; i++) {
                EconomyCreature base = factory.create(false, i, 1);
                EconomyCreature upgraded = factory.create(true, i, 1);

                CreatureButton baseButton = new CreatureButton(this, factory, false, i);
                CreatureButton upgradedButton = new CreatureButton(this, factory, true, i);

                BastionUpgradeBuildings baseDwelling = BASTION_BASE_DWELLING.get(base.getStats());
                if (baseDwelling == null || !town.hasBuilt(baseDwelling)) {
                    baseButton.setDisable(true);
                }

                BastionUpgradeBuildings upgradedDwelling = BASTION_UPGRADED_DWELLING.get(upgraded.getStats());
                if (upgradedDwelling == null || !town.hasBuilt(upgradedDwelling)) {
                    upgradedButton.setDisable(true);
                }

                creatureShop.getChildren().addAll(baseButton, upgradedButton);
            }
        } else {
            final EconomyNecropolisFactory factory = new EconomyNecropolisFactory();

            for (int i = 1; i < 8; i++) {
                EconomyCreature base = factory.create(false, i, 1);
                EconomyCreature upgraded = factory.create(true, i, 1);

                CreatureButton baseButton = new CreatureButton(this, factory, false, i);
                CreatureButton upgradedButton = new CreatureButton(this, factory, true, i);

                UpgradeBuildings.getBuildingForCreature(base.getStats())
                        .ifPresentOrElse(
                                building -> {
                                    if (!town.hasBuilt(building)) {
                                        baseButton.setDisable(true);
                                    }
                                },
                                () -> baseButton.setDisable(true)
                        );

                UpgradeBuildings.getBuildingForCreature(upgraded.getStats())
                        .ifPresentOrElse(
                                building -> {
                                    if (!town.hasBuilt(building)) {
                                        upgradedButton.setDisable(true);
                                    }
                                },
                                () -> upgradedButton.setDisable(true)
                        );

                creatureShop.getChildren().addAll(baseButton, upgradedButton);
            }
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

    public void buy( final EconomyCreature aCreature )
    {
        economyEngine.buy( aCreature );
    }

    @Override
    public void propertyChange( final PropertyChangeEvent aPropertyChangeEvent )
    {
        refreshGui();
    }
}