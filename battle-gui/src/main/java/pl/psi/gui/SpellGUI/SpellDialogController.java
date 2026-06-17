package pl.psi.gui.SpellGUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.psi.Spells.Spell;

import java.util.List;
import java.util.function.Consumer;

public class SpellDialogController {

    @FXML
    private VBox spellList;
    @FXML
    private Button okButton;

    private Spell selectedSpell;
    private Consumer<Spell> onSpellChosen;

    public void setSpells(List<Spell> spells, Consumer<Spell> onSpellChosen) {
        this.onSpellChosen = onSpellChosen;

        ToggleGroup group = new ToggleGroup();
        for (Spell spell : spells) {
            RadioButton radio = new RadioButton(spell.getName());
            radio.setToggleGroup(group);
            radio.setUserData(spell);

            radio.setOnMouseClicked(e -> {
                if (group.getSelectedToggle() == radio && selectedSpell == spell) {
                    group.selectToggle(null);
                    selectedSpell = null;
                } else {
                    selectedSpell = spell;
                }
            });
            spellList.getChildren().add(radio);
        }

        okButton.setOnAction(e -> {
            if (selectedSpell != null && onSpellChosen != null) {
                onSpellChosen.accept(selectedSpell);
                // zamknij okno
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
            }
        });
    }
}

