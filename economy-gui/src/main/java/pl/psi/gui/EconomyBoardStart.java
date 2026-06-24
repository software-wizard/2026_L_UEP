package pl.psi.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.psi.gui.startchoice.HeroSelection;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.artifacts.ArtifactType;
import pl.psi.hero.artifacts.EconomySpell;
import pl.psi.map.MapObjectIf;
import pl.psi.map.buildings.town.Town;
import pl.psi.map.buildings.bank.Bank;
import pl.psi.map.buildings.bank.BankStatistics;
import pl.psi.map.resources.Gold;
import pl.psi.map.resources.Resources;
import pl.psi.map.resources.generators.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EconomyBoardStart extends Application {

    EconomyHero hero1;
    EconomyHero hero2;

    public static final String SPELL_NAME = "Default";

    public static void main(final String[] args )
    {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        HeroSelection.showAndWait().thenAccept(heroes -> {
            hero1 = heroes[0];
            hero2 = heroes[1];

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/economy-board.fxml"));
                loader.setController(new EconomyBoardController(hero1, hero2, map()));
                Scene scene = new Scene(loader.load());

                primaryStage.setScene(scene);
                primaryStage.setTitle("Mapa Ekonomiczna");
                primaryStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<pl.psi.economy.Point, MapObjectIf> map() {
        Map<pl.psi.economy.Point, MapObjectIf> map = new HashMap<>();

        map.put(new pl.psi.economy.Point(4,4), new Artifact(ArtifactType.SWORD_OF_HELLFIRE));
        map.put(new pl.psi.economy.Point(17,1), new Town(hero1));
        map.put(new pl.psi.economy.Point(1,7), new Town(hero2));
        map.put(new pl.psi.economy.Point(3,2), new ResourceGenerator(ResourceGenType.GEM));
        map.put(new pl.psi.economy.Point(5,6), new ResourceGenerator(ResourceGenType.GOLD));
        map.put(new pl.psi.economy.Point(8,1), new ResourceGenerator(ResourceGenType.MERCURY));
        map.put(new pl.psi.economy.Point(10,4), new ResourceGenerator(ResourceGenType.WOOD));
        map.put(new pl.psi.economy.Point(13,7), new ResourceGenerator(ResourceGenType.SULFUR));
        map.put(new pl.psi.economy.Point(15,2), new ResourceGenerator(ResourceGenType.CRYSTAL));
        map.put(new pl.psi.economy.Point(6,8), new ResourceGenerator(ResourceGenType.ORE));
        // map.put(new pl.psi.economy.Point(9,3), new Gold(new Resources(1000,0,0,0,0,0,0)));
        // map.put(new pl.psi.economy.Point(10,6), new Gold(new Resources(1000,0,0,0,0,0,0)));
        map.put(new pl.psi.economy.Point(2,2), new Bank(BankStatistics.CASTLE_1));
        map.put(new pl.psi.economy.Point(8,8), new Bank(BankStatistics.CASTLE_2));
        // map.put(new pl.psi.economy.Point(8,5), new EconomySpell(SPELL_NAME));

        return map;
    }
}
