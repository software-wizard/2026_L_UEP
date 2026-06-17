package pl.psi.gui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
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
import pl.psi.BattlePoint;
import pl.psi.MapData;
import pl.psi.MapPersistenceManager;
import pl.psi.SpecialField;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapEditorController {
    private final int width = 15;
    private final int height = 11;
    private final BiMap<BattlePoint, SpecialField.FieldName> placedFields = HashBiMap.create();
    private final MapPersistenceManager persistenceManager = new MapPersistenceManager();
    
    private ComboBox<String> toolSelector;
    private GridPane gridPane;

    public void show() {
        Stage stage = new Stage();
        stage.setTitle("Heroes 3 - Combat Map Editor");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top Toolbar
        HBox toolbar = new HBox(15);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(0, 0, 10, 0));

        Label toolLabel = new Label("Select Special Field:");
        toolSelector = new ComboBox<>();
        List<String> tools = new ArrayList<>();
        tools.add("ERASER");
        for (SpecialField.FieldName name : SpecialField.FieldName.values()) {
            tools.add(name.name());
        }
        toolSelector.setItems(FXCollections.observableArrayList(tools));
        toolSelector.setValue("QUICKSAND");

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

        Scene scene = new Scene(root, 900, 680);
        stage.setScene(scene);
        stage.show();
    }

    private void refreshGrid() {
        gridPane.getChildren().clear();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final int finalX = x;
                final int finalY = y;
                
                BattlePoint p = new BattlePoint(x, y);
                SpecialField.FieldName currentField = placedFields.get(p);
                
                StackPane tile = createTileView(currentField);
                tile.setOnMouseClicked(e -> {
                    String selectedTool = toolSelector.getValue();
                    if ("ERASER".equals(selectedTool)) {
                        placedFields.remove(p);
                    } else {
                        placedFields.put(p, SpecialField.FieldName.valueOf(selectedTool));
                    }
                    refreshGrid();
                });
                
                gridPane.add(tile, x, y);
            }
        }
    }

    private StackPane createTileView(SpecialField.FieldName fieldName) {
        StackPane tile = new StackPane();
        tile.setPrefSize(50, 50);
        
        Rectangle rect = new Rectangle(50, 50);
        rect.setStroke(Color.LIGHTGRAY);
        rect.setStrokeWidth(1);
        
        Label label = new Label("");
        label.setStyle("-fx-font-size: 8px; -fx-text-alignment: center; -fx-wrap-text: true;");
        
        if (fieldName == null) {
            rect.setFill(Color.WHITE);
        } else {
            rect.setFill(getColorForField(fieldName));
            label.setText(fieldName.name());
        }
        
        tile.getChildren().addAll(rect, label);
        return tile;
    }

    private Color getColorForField(SpecialField.FieldName fieldName) {
        switch (fieldName) {
            case QUICKSAND:
                return Color.YELLOW;
            case MAGIC_PLAINS:
                return Color.CYAN;
            case DMG_FIELD:
                return Color.BROWN;
            case DEBUFF_FIELD:
                return Color.LIGHTGRAY;
            case BUFF_FIELD:
                return Color.ORANGE;
            case FIELD_CAN_ONLY_BE_FLOWN:
                return Color.GOLD;
            case FIRE_FIELD:
                return Color.RED;
            case CRACKED_ICE:
                return Color.ALICEBLUE;
            case FIELDS_OF_GLORY:
                return Color.DARKGRAY;
            case SPELL_FIELD:
                return Color.LIGHTBLUE;
            default:
                return Color.WHITE;
        }
    }

    private void handleSave(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Combat Map");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                // Convert internal placedFields to MapData
                BiMap<BattlePoint, SpecialField> fieldsModelMap = HashBiMap.create();
                for (BattlePoint p : placedFields.keySet()) {
                    fieldsModelMap.put(p, MapPersistenceManager.createField(placedFields.get(p)));
                }
                MapData mapData = persistenceManager.convertToMapData(fieldsModelMap, width, height);
                persistenceManager.saveMap(mapData, file);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Map saved successfully!", ButtonType.OK);
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
        fileChooser.setTitle("Load Combat Map");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                MapData mapData = persistenceManager.loadMap(file);
                BiMap<BattlePoint, SpecialField> fieldsModelMap = persistenceManager.convertToBiMap(mapData);
                
                placedFields.clear();
                for (BattlePoint p : fieldsModelMap.keySet()) {
                    placedFields.put(p, fieldsModelMap.get(p).getFieldName());
                }
                
                refreshGrid();
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Map loaded successfully!", ButtonType.OK);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load map: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
}
