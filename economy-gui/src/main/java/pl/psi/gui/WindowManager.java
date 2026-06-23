package pl.psi.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType; //ddoane tutaj
import javafx.stage.Stage;
import pl.psi.gui.Upgrades.UpgradeController;
import pl.psi.gui.shops.*;
import pl.psi.hero.EconomyHero;
import pl.psi.map.buildings.town.Town;

import java.io.IOException;

public class WindowManager {

    public static void openShop(EconomyHero hero, Town town) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource("/fxml/shop-category.fxml"));
            Parent root = loader.load();

            ShopCategoryController controller = loader.getController();
            controller.init(hero, town);  // Przekazujemy dane po załadowaniu FXML

            Stage stage = new Stage();
            stage.setTitle("Choose Shop");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void openUpgrades(EconomyHero hero, Town town) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource("/fxml/upgrades.fxml"));
            loader.setControllerFactory(param -> new UpgradeController());
            Parent root = loader.load();
            UpgradeController controller = loader.getController();
            controller.setData(hero, town); // Pass your objects safely

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (final IOException aE) {
            aE.printStackTrace();
        }
    }

    public static void openCreatureShop(EconomyHero hero, Town town) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WindowManager.class.getClassLoader()
                    .getResource("fxml/creature-shop.fxml"));
            loader.setController(new CreatureShopController(hero, town));

            final Scene scene = new Scene(loader.load());
            Stage aStage = new Stage();
            aStage.setScene(scene);
            aStage.setX(5);
            aStage.setY(5);
            aStage.show();
        } catch (final IOException aE) {
            aE.printStackTrace();
        }
    }

    public static void openSpellShop(EconomyHero hero, Town town) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WindowManager.class.getClassLoader()
                    .getResource("fxml/spell-shop.fxml"));
            loader.setController(new SpellShopController(hero, town));

            final Scene scene = new Scene(loader.load());
            Stage aStage = new Stage();
            aStage.setScene(scene);
            aStage.setX(5);
            aStage.setY(5);
            aStage.show();
        } catch (final IOException aE) {
            aE.printStackTrace();
        }
    }
    public static void openArtifactShop(EconomyHero hero, Town town) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WindowManager.class.getClassLoader()
                    .getResource("fxml/artifact-shop.fxml"));
            loader.setController(new ArtifactShopController(hero, town));

            final Scene scene = new Scene(loader.load());
            Stage aStage = new Stage();
            aStage.setScene(scene);
            aStage.setX(5);
            aStage.setY(5);
            aStage.show();
        } catch (final IOException aE) {
            aE.printStackTrace();
        }
    }
    public static void openSkillShop(EconomyHero hero, Town town) {
        try {
            final FXMLLoader loader = new FXMLLoader();
            loader.setLocation(WindowManager.class.getClassLoader()
                    .getResource("fxml/skill-shop.fxml"));
            loader.setController(new SkillShopController(hero, town));

            final Scene scene = new Scene(loader.load());
            Stage aStage = new Stage();
            aStage.setScene(scene);
            aStage.setX(5);
            aStage.setY(5);
            aStage.show();
        } catch (final IOException aE) {
            aE.printStackTrace();
        }
    }

    public static void openEquipment(EconomyHero hero) {
        try {
            FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource("/fxml/equipment.fxml"));
            loader.setControllerFactory(param -> new EquipmentController(hero)); // hero injected
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Hero Equipment");
            stage.show();

        } catch (final IOException aE) {
            aE.printStackTrace();
        }
    }

    public static void openForge(EconomyHero aHero) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Kuźnia Maszyn Wojennych");
        alert.setHeaderText("Kuźnia");
        alert.setContentText("Wybierz maszynę wojenną, którą chcesz zakupić do swojej armii:");

        ButtonType buyBallista = new ButtonType("Balista (2500 Gold)");
        ButtonType buyTent = new ButtonType("Namiot Medyczny (750 Gold)");
        ButtonType buyCart = new ButtonType("Wóz z Amunicją (1000 Gold)");
        ButtonType cancel = new ButtonType("Wyjdź");

        alert.getButtonTypes().setAll(buyBallista, buyTent, buyCart, cancel);

        alert.showAndWait().ifPresent(type -> {
            int currentGold = aHero.getResources().getGold();

            if (type == buyBallista) {
                if (aHero.canAffordGold(2500)) {
                    aHero.payGold(2500);

                    aHero.setHasBallista(true);

                    System.out.println("Kupiono Balistę!");
                } else {
                    showError("Masz za mało złota!");
                }
            } else if (type == buyTent) {
                if (aHero.canAffordGold(750)) {
                    aHero.payGold(750);
                    // TODO: Dodanie namiotu
                    System.out.println("Kupiono Namiot Medyczny!");
                } else {
                    showError("Masz za mało złota!");
                }
            } else if (type == buyCart) {
                if (aHero.canAffordGold(1000)) {
                    aHero.payGold(1000);
                    // TODO: Dodanie wozu
                    System.out.println("Kupiono Wóz z Amunicją!");
                } else {
                    showError("Masz za mało złota!");
                }
            }
        });
    }

    private static void showError(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Błąd");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}
