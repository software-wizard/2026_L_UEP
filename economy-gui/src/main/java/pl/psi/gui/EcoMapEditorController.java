package pl.psi.gui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.psi.economy.Point;
import pl.psi.map.EcoMapData;
import pl.psi.map.EcoMapObjectDto;
import pl.psi.map.EcoMapPersistenceManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EcoMapEditorController {
    private final int width = 18;
    private final int height = 9;
    private final Map<Point, EcoMapObjectDto> placedObjects = new HashMap<>();
    private final EcoMapPersistenceManager persistenceManager = new EcoMapPersistenceManager();

    private ComboBox<String> toolSelector;
    private GridPane gridPane;

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Heroes 3 - Economy Map Editor");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top Toolbar
        HBox toolbar = new HBox(15);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 0, 10, 0));

        Label toolLabel = new Label("Select Object to Place:");
        toolSelector = new ComboBox<>();
        
        List<String> tools = new ArrayList<>();
        tools.add("ERASER");
        tools.add("TOWN:PLAYER1");
        tools.add("TOWN:PLAYER2");
        tools.add("RESOURCE_GENERATOR:GEM");
        tools.add("RESOURCE_GENERATOR:GOLD");
        tools.add("RESOURCE_GENERATOR:MERCURY");
        tools.add("RESOURCE_GENERATOR:WOOD");
        tools.add("RESOURCE_GENERATOR:SULFUR");
        tools.add("RESOURCE_GENERATOR:CRYSTAL");
        tools.add("RESOURCE_GENERATOR:ORE");
        tools.add("GOLD:1000");
        tools.add("BANK:CASTLE_1");
        tools.add("BANK:CASTLE_2");
        tools.add("SPELL:Default");
        tools.add("ARTIFACT:SWORD_OF_HELLFIRE");
        tools.add("ARTIFACT:ARMOR_OF_WONDER");
        
        toolSelector.setItems(FXCollections.observableArrayList(tools));
        toolSelector.setValue("TOWN:PLAYER1");

        Button saveButton = new Button("Save Map");
        saveButton.setOnAction(e -> handleSave(stage));

        Button loadButton = new Button("Load Map");
        loadButton.setOnAction(e -> handleLoad(stage));

        toolbar.getChildren().addAll(toolLabel, toolSelector, saveButton, loadButton);
        root.setTop(toolbar);

        // Center Grid
        gridPane = new GridPane();
        gridPane.setHgap(3);
        gridPane.setVgap(3);
        gridPane.setAlignment(Pos.CENTER);

        refreshGrid();
        root.setCenter(gridPane);

        Scene scene = new Scene(root, 1100, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void refreshGrid() {
        gridPane.getChildren().clear();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int finalX = x;
                final int finalY = y;

                Point p = new Point(x, y);
                EcoMapObjectDto currentObj = placedObjects.get(p);

                StackPane tile = createTileView(currentObj);
                tile.setOnMouseClicked(e -> {
                    String selectedTool = toolSelector.getValue();
                    if ("ERASER".equals(selectedTool)) {
                        placedObjects.remove(p);
                    } else {
                        String[] parts = selectedTool.split(":");
                        placedObjects.put(p, new EcoMapObjectDto(parts[0], parts[1]));
                    }
                    refreshGrid();
                });

                gridPane.add(tile, x, y);
            }
        }
    }

    private StackPane createTileView(EcoMapObjectDto dto) {
        StackPane tile = new StackPane();
        tile.setPrefSize(55, 50);

        Rectangle rect = new Rectangle(55, 50);
        rect.setStroke(Color.LIGHTGRAY);
        rect.setStrokeWidth(1);

        Label label = new Label("");
        label.setStyle("-fx-font-size: 8px; -fx-text-alignment: center; -fx-wrap-text: true;");

        if (dto == null) {
            rect.setFill(Color.WHITE);
        } else {
            rect.setFill(getColorForObject(dto.getType(), dto.getParam()));
            label.setText(dto.getType() + "\n(" + dto.getParam() + ")");
        }

        tile.getChildren().addAll(rect, label);
        return tile;
    }

    private Color getColorForObject(String type, String param) {
        switch (type) {
            case "TOWN":
                return "PLAYER2".equalsIgnoreCase(param) ? Color.RED : Color.LIGHTGREEN;
            case "RESOURCE_GENERATOR":
                return Color.DARKGREY;
            case "GOLD":
                return Color.GOLD;
            case "BANK":
                return Color.PLUM;
            case "SPELL":
                return Color.LIGHTBLUE;
            case "ARTIFACT":
                return Color.ORANGE;
            default:
                return Color.WHITE;
        }
    }

    private void handleSave(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Economy Map");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                Map<String, EcoMapObjectDto> stringMap = new HashMap<>();
                for (Map.Entry<Point, EcoMapObjectDto> entry : placedObjects.entrySet()) {
                    String key = entry.getKey().getX() + "," + entry.getKey().getY();
                    stringMap.put(key, entry.getValue());
                }
                EcoMapData mapData = new EcoMapData(width, height, stringMap);
                persistenceManager.saveMap(mapData, file);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Economy Map saved successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save map: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    private void handleLoad(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Economy Map");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                EcoMapData mapData = persistenceManager.loadMap(file);
                placedObjects.clear();
                for (Map.Entry<String, EcoMapObjectDto> entry : mapData.getObjects().entrySet()) {
                    String[] coords = entry.getKey().split(",");
                    int x = Integer.parseInt(coords[0].trim());
                    int y = Integer.parseInt(coords[1].trim());
                    placedObjects.put(new Point(x, y), entry.getValue());
                }

                refreshGrid();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Economy Map loaded successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load map: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
}
