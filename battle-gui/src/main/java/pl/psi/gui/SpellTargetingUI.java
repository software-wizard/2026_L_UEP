package pl.psi.gui;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import pl.psi.BattlePoint;
import pl.psi.Spells.Spell;
import pl.psi.gui.SpellGUI.SpellCastingManager;
import pl.psi.gui.SpellGUI.SpellUIManager;

import java.util.List;

public class SpellTargetingUI {

    public static void attachTargeting(MapTile mapTile, BattlePoint currentBattlePoint, SpellCastingManager spellManager, SpellUIManager spellUIManager, GridPane gridMap) {
        
        mapTile.setOnMouseClicked(e -> spellUIManager.confirmSpellCast(currentBattlePoint));

        Spell spell = spellManager.getSelectedSpell();

        mapTile.setOnMouseEntered(e -> {
            List<BattlePoint> area = spell.getAreaStrategy().getArea(currentBattlePoint);
            highlightArea(gridMap, area, true);
        });

        mapTile.setOnMouseExited(e -> {
            List<BattlePoint> area = spell.getAreaStrategy().getArea(currentBattlePoint);
            highlightArea(gridMap, area, false);
        });
    }

    private static void highlightArea(GridPane gridMap, List<BattlePoint> area, boolean isEntering) {
        for (Node node : gridMap.getChildren()) {
            if (node instanceof MapTile) {
                Integer col = GridPane.getColumnIndex(node);
                Integer row = GridPane.getRowIndex(node);
                int x = (col == null) ? 0 : col;
                int y = (row == null) ? 0 : row;
                
                BattlePoint p = new BattlePoint(x, y);
                if (area.contains(p)) {
                    MapTile tile = (MapTile) node;
                    if (isEntering) {
                        tile.setHighlightColor(Color.web("0xFF00FF", 0.5)); // Magenta overlay
                    } else {
                        tile.clearHighlight();
                    }
                }
            }
        }
    }
}
