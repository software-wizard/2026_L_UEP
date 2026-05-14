package pl.psi.gui;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.artifacts.ArtifactType;

public class EquipmentController {

    @FXML
    private VBox equipmentList;

    private final EconomyHero hero;

    public EquipmentController(EconomyHero hero) {
        this.hero = hero;
    }

    @FXML
    private void initialize() {
        showEquipment();
    }

    private void showEquipment() {
        equipmentList.getChildren().clear();

        for (Artifact artifact : hero.getArtifacts()) {
            VBox artifactBox = new VBox(2);
            artifactBox.setStyle("-fx-padding: 5; -fx-border-color: #ccc;");

            try {
                ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream(artifact.getPath())));
                imageView.setFitWidth(50);
                imageView.setPreserveRatio(true);
                artifactBox.getChildren().add(imageView);
            } catch (Exception e) {
                System.err.println("Could not load image for artifact: " + artifact.getPath());
            }

            Text stats = new Text(artifact.getStats().toString());
            Text name = new Text(artifact.toString());

            artifactBox.getChildren().addAll(name, stats);
            equipmentList.getChildren().add(artifactBox);
        }
    }
}
